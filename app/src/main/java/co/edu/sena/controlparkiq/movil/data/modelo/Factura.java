package co.edu.sena.controlparkiq.movil.data.modelo;

import java.math.BigDecimal;

/** Factura del parqueo de un visitante: horas cobradas, valor y pago. */
public class Factura {

    private Long id;
    private Sesion sesion;
    private Integer horasCobradas;
    private BigDecimal valorHora;
    private BigDecimal valorTotal;
    private String fechaEmision;
    private boolean pagada;
    private String metodoPago;
    private String fechaPago;

    public Long getId() {
        return id;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public Integer getHorasCobradas() {
        return horasCobradas;
    }

    public BigDecimal getValorHora() {
        return valorHora;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public boolean isPagada() {
        return pagada;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }
}
