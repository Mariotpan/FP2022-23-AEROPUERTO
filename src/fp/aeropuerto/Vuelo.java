package fp.aeropuerto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Vuelo implements Comparable<Vuelo>{
	private LocalDate fecha;
	private LocalTime horaPlanificada;
	private String codigo;
	private String ciudad;
	private String codigoAeropuerto;
	private String compania;
	private String modeloAvion;
	private String idAvion;
	private EstadoVuelo estado;
	private LocalTime horaEfectiva;
	private DireccionVuelo direccion;
	
	public Long diferenciaEnMinutos() {
		Long diferencia = null;
		if(!estado.equals(EstadoVuelo.CANCELED)) {
			diferencia = horaEfectiva.until(horaPlanificada, ChronoUnit.MINUTES);
		}
		return diferencia;
	}

	public Vuelo(LocalDate fecha, LocalTime horaPlanificada, String codigo, String ciudad, String codigoAeropuerto,
			String compania, String modeloAvion, String idAvion, EstadoVuelo estado, LocalTime horaEfectiva,
			DireccionVuelo direccion) {
		super();
		this.fecha = fecha;
		this.horaPlanificada = horaPlanificada;
		this.codigo = codigo;
		this.ciudad = ciudad;
		this.codigoAeropuerto = codigoAeropuerto;
		this.compania = compania;
		this.modeloAvion = modeloAvion;
		this.idAvion = idAvion;
		this.estado = estado;
		this.horaEfectiva = horaEfectiva;
		this.direccion = direccion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, fecha, horaPlanificada);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vuelo other = (Vuelo) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(horaPlanificada, other.horaPlanificada);
	}

	public int compareTo(Vuelo v) {
		int r;
		if(v == null)
			throw new NullPointerException();
		r = getFecha().compareTo(v.getFecha());
		if(r == 0) {
			r = getHoraPlanificada().compareTo(v.getHoraPlanificada());
			if(r == 0) {
				r = getCodigo().compareTo(v.getCodigo());
			}
		}
		return r;
	}
	
	@Override
	public String toString() {
		return "Vuelo [fecha=" + fecha + ", horaPlanificada=" + horaPlanificada + ", codigo=" + codigo + "]";
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHoraPlanificada() {
		return horaPlanificada;
	}
	
	public Integer getHoraPlanificadaHora() {
		return horaPlanificada.getHour();
	}

	public void setHoraPlanificada(LocalTime horaPlanificada) {
		this.horaPlanificada = horaPlanificada;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCodigoAeropuerto() {
		return codigoAeropuerto;
	}

	public void setCodigoAeropuerto(String codigoAeropuerto) {
		this.codigoAeropuerto = codigoAeropuerto;
	}

	public String getCompania() {
		return compania;
	}

	public void setCompania(String compania) {
		this.compania = compania;
	}

	public String getModeloAvion() {
		return modeloAvion;
	}

	public void setModeloAvion(String modeloAvion) {
		this.modeloAvion = modeloAvion;
	}

	public String getIdAvion() {
		return idAvion;
	}

	public void setIdAvion(String idAvion) {
		this.idAvion = idAvion;
	}

	public EstadoVuelo getEstado() {
		return estado;
	}

	public void setEstado(EstadoVuelo estado) {
		this.estado = estado;
	}

	public LocalTime getHoraEfectiva() {
		return horaEfectiva;
	}

	public void setHoraEfectiva(LocalTime horaEfectiva) {
		this.horaEfectiva = horaEfectiva;
	}

	public DireccionVuelo getDireccion() {
		return direccion;
	}

	public void setDireccion(DireccionVuelo direccion) {
		this.direccion = direccion;
	}

	
}
