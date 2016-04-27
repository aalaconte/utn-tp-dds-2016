package model;

import org.joda.time.LocalTime;
import org.junit.Test;

import junit.framework.Assert;

public class RangoHorarioTest {

    RangoHorario rangoHorario = new RangoHorario();

    @Test
    public void dadaUnaHoraDentroDeUnRangoDeberiaEstarIncluidaEnElRango() {
        LocalTime unaHora = new LocalTime(12, 30);
        LocalTime horaInicioRango = new LocalTime(12, 00);
        LocalTime horaFinRango = new LocalTime(13, 00);
        rangoHorario.setHoraInicio(horaInicioRango);
        rangoHorario.setHoraFin(horaFinRango);

        Assert.assertTrue(rangoHorario.incluye(unaHora));

    }

    @Test
    public void dadaUnaHoraFueraDeUnRangoNoDeberiaEstarIncluidaEnElRango() {
        LocalTime unaHora = new LocalTime(11, 59);
        LocalTime horaInicioRango = new LocalTime(12, 00);
        LocalTime horaFinRango = new LocalTime(13, 00);
        rangoHorario.setHoraInicio(horaInicioRango);
        rangoHorario.setHoraFin(horaFinRango);

        Assert.assertFalse(rangoHorario.incluye(unaHora));

    }
}
