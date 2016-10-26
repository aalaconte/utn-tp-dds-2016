package ar.edu.utn.frba.dds.model.poi.sucursal.banco;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import ar.edu.utn.frba.dds.model.poi.Horarios;
import ar.edu.utn.frba.dds.model.poi.PuntoDeInteres;
import ar.edu.utn.frba.dds.model.poi.RangoHorario;
import ar.edu.utn.frba.dds.model.poi.TipoPoi;
import ar.edu.utn.frba.dds.util.time.DateTimeProvider;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class SucursalBanco extends PuntoDeInteres {

    private String banco;
    private String sucursal;
    private String gerente;
	@OneToMany(fetch=FetchType.EAGER) @Cascade(value = CascadeType.ALL)
	@JoinColumn(name = "idBanco", referencedColumnName = "id")
    private Set<ServicioBanco> servicios;
@Transient
    private Horarios horarios;
    private String tipo = TipoPoi.SUCURSAL_BANCO.toString();

    public SucursalBanco(){
    	
    }
    
    public SucursalBanco(DateTimeProvider dateTimeProviderImpl) {
        this.dateTimeProvider = dateTimeProviderImpl;
        palabrasClave = new HashSet<>();
        //id = contador.incrementAndGet();
        this.horarios = new Horarios();
        LocalTime horaInicioLunesAViernes = new LocalTime(10, 0);
        LocalTime horaFinLunesAViernes = new LocalTime(15, 0);
        RangoHorario manianaLunesAViernes = new RangoHorario(horaInicioLunesAViernes, horaFinLunesAViernes);
        horarios.agregarRangoHorario(1, manianaLunesAViernes);
        horarios.agregarRangoHorario(2, manianaLunesAViernes);
        horarios.agregarRangoHorario(3, manianaLunesAViernes);
        horarios.agregarRangoHorario(4, manianaLunesAViernes);
        horarios.agregarRangoHorario(5, manianaLunesAViernes);
    }

    public String getBanco() {
        return banco;
    }

    public Horarios getHorarios() {
		return horarios;
	}

	public void setHorarios(Horarios horarios) {
		this.horarios = horarios;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setBanco(final String banco) {
        this.banco = banco;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getGerente() {
        return gerente;
    }

    public void setGerente(String gerente) {
        this.gerente = gerente;
    }

    public Set<ServicioBanco> getServicios() {
        return servicios;
    }

    public void setServicios(final Set<ServicioBanco> servicios) {
        this.servicios = servicios;
    }

    @Override
    public boolean estaDisponible() {
        DateTime fechaHoraActual = this.dateTimeProvider.getDateTime();
        for (ServicioBanco servicio : servicios) {
            if (servicio.getHorarios().atiende(fechaHoraActual)) {
                return true;
            }
        }
        return horarios.atiende(fechaHoraActual);
    }

    public boolean estaDisponible(final String nombreServicioBanco) {
        for (ServicioBanco servicio : servicios) {
            if (servicio.getNombre() == nombreServicioBanco) {
                return servicio.estaDisponible(this.dateTimeProvider.getDateTime()) && this.estaDisponible();
            }
        }
        return false;
    }

    @Override
    public boolean tienePalabra(final String palabra) {
        boolean bancoTienePalabra = this.getBanco().toLowerCase().contains(palabra.toLowerCase());
        boolean servicioTienePalabra = serviciosTienenPalabra(palabra);
        boolean esPalabraClave = this.esPalabraClave(palabra);
        return (bancoTienePalabra || servicioTienePalabra || esPalabraClave);
    }

    private boolean serviciosTienenPalabra(final String palabra) {
        for (ServicioBanco servicio : servicios) {
            if (servicio.getNombre().toLowerCase().contains(palabra.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("Banco: ").append(banco);
        ret.append(". Sucursal: ").append(sucursal);
        ret.append(". Gerente: ").append(gerente);
        ret.append(". Geolocalizacion: ").append(geolocalizacion.toString());
        ret.append(". Servicios: ");
        for (ServicioBanco servicio : servicios) {
            ret.append(servicio.getNombre()).append(", ");
        }
        ret.delete(ret.length() - 2, ret.length());
        return ret.toString();
    }

    @Override
    public String getNombre() {
        return banco;
    }

    @Override
    public String getTipo() {
        return tipo;
    }
}
