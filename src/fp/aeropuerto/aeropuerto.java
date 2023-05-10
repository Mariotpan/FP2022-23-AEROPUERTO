package fp.aeropuerto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class aeropuerto {
	private SortedSet<Vuelo> vuelos;

	public aeropuerto() {
		this.vuelos = new TreeSet<Vuelo>();
	}
	
	public aeropuerto(Stream<Vuelo> vuelos) {
		this.vuelos = vuelos.collect(Collectors.toCollection(TreeSet::new));
	}
	
	public aeropuerto(SortedSet<Vuelo> vuelos) {
		this.vuelos = new TreeSet<Vuelo>(vuelos);
	}
	
	public SortedSet<Vuelo> getVuelos() {
		return new TreeSet<Vuelo>(this.vuelos);
	}
	
	public void anadirVuelo (Vuelo v) {
		vuelos.add(v);
	}
	
	//indica si existe algún vuelo que tiene como destino la ciudad dada.
	public  Boolean existeVueloDestino(String ciudad) {
		return vuelos.stream()
					 .anyMatch(x->x.getCiudad().equals(ciudad) && x.getDireccion().equals(DireccionVuelo.OUT));
	}
	
	/*
	 * indica si todos los vuelos
		de la compañía dada son puntuales (su hora efectiva es igual o anterior a su hora planificada).*/
	
	public  Boolean todosVuelosCompañiaSinRetraso(String compañia) {
		return vuelos.stream()
					 .filter(x->x.getCompania().equals(compañia))
					 .allMatch(x-> x.diferenciaEnMinutos()<=0);
	}
	
	/*obtiene el número de vuelos de salida.*/
	public Long getNumeroVuelosSalida() {
		return vuelos.stream()
					 .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
					 .count();
	}
	
	/*obtiene el número de vuelos que tienen como destino la ciudad dada*/
	public Long getNumeroVuelosDestino(String ciudad) {
		return vuelos.stream()
					 .filter(x->x.getCiudad().equals(ciudad) && x.getDireccion().equals(DireccionVuelo.OUT))
					 .count();
	}
	
	/*------------------------*/
	
	/*obtiene el número de vuelos cancelados.*/
	public Long getNumeroVuelosCancelados() {
		return vuelos.stream()
					 .filter(x->x.getEstado().equals(EstadoVuelo.CANCELED))
					 .count();
	}
	
	/*obtiene el número de vuelos operados por la compañía dada.*/
	public Long getNumeroVuelosCompañia(String compañia) {
		return vuelos.stream()
					 .filter(x->x.getCompania().equals(compañia))
					 .count();
	}
	
	/*obtiene el número de vuelos con retraso, entendiendo por tales aquellos cuya hora efectiva es posterior a su hora planificada.
	No se incluyen los vuelos cancelados.*/
	public Long getNumeroVuelosConRetraso() {
		return vuelos.stream()	
					 .filter(x->!x.getEstado().equals(EstadoVuelo.CANCELED))
					 .filter(x->x.diferenciaEnMinutos()>0)
					 .count();
	}
	
	/*obtiene un conjunto con las compañías aéreas diferentes que operan en el aeropuerto.*/
	public Set<String> getCompañiasVuelos(){
		return vuelos.stream()
					 .map(Vuelo::getCompania)
					 .collect(Collectors.toSet());
	}
	
	/*obtiene el número de ciudades diferentes a las que se puede volar desde el aeropuerto*/
	public Long getNumeroCiudadesDestino() {
		return vuelos.stream()
					 .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
					 .map(Vuelo::getCiudad)
					 .distinct()
					 .count();
	}
	
	/*obtiene un conjunto con los modelos de aviones utilizados por una compañía dada.*/
	public Set<String> getModelosAvionesCompañia(String compañia){
		return vuelos.stream()		
					 .filter(x->x.getCompania().equals(compañia))
					 .map(Vuelo::getModeloAvion)
					 .collect(Collectors.toSet());
	}
	
	/*obtiene el retraso acumulado de todos los vuelos operados por la compañía dada, en minutos. 
	 * No se incluyen los vuelos cancelados.*/
	public Long getMinutosRetrasoAcumuladosCompañia(String compañia) {
		return vuelos.stream()
					 .filter(x->x.getCompania().equals(compañia))
					 .filter(x->!x.getEstado().equals(EstadoVuelo.CANCELED))
					 .filter(x->x.diferenciaEnMinutos()<0)
					 .mapToLong(Vuelo::diferenciaEnMinutos)
					 .sum();
					 
					 
	}
	
	/*obtiene el retraso medio de todos los vuelos que tienen por destino una ciudad. 
	No se incluyen los vuelos cancelados.*/
	public Long getMediaMinutosRetrasoCiudad(String ciudad) {
		Double res =  vuelos.stream()
				  	 .filter(x->x.getCiudad().equals(ciudad) && x.getDireccion().equals(DireccionVuelo.OUT))
				  	 .filter(x->!x.getEstado().equals(EstadoVuelo.CANCELED))
				  	 .mapToDouble(Vuelo::diferenciaEnMinutos).average().getAsDouble();
		
		return Long.valueOf(res.toString());
				  	 
	}
	
	/*obtiene los días de la semana en los que salen vuelos hacia un destino.*/
	public Set<DayOfWeek> getDiasSemanaConVuelosDestino(String ciudad){
		return vuelos.stream()
					 .filter(x-> x.getCiudad().equals(ciudad) && x.getDireccion().equals(DireccionVuelo.OUT))
					 .map(x->x.getFecha().getDayOfWeek())
					 .collect(Collectors.toSet());
					 
	}
	
	/*obtiene el vuelo con más minutos de retraso. En caso de igualdad, desempatar por el orden natural.*/
	public Vuelo getVueloMayorRetraso() {
		Comparator<Vuelo> cmp = Comparator.comparing(Vuelo::diferenciaEnMinutos).thenComparing(Comparator.naturalOrder());
		
		return vuelos.stream()
				.filter(x->x.diferenciaEnMinutos()<0)
				.min(cmp)
				.orElse(null);
	}
	
	/*obtiene el vuelo con destino la ciudad dada que tiene una hora planificada anterior. 
	En caso de igualdad, desempatar por el orden natural.*/
	public Vuelo getVueloMasTempranoDestino(String ciudad) {
		Comparator<Vuelo> cmp = Comparator.comparing(Vuelo::getHoraPlanificada).thenComparing(Comparator.naturalOrder());
		
		return vuelos.stream()
					 .filter(x->x.getCiudad().equals(ciudad) && x.getDireccion().equals(DireccionVuelo.OUT))
					 .min(cmp)
					 .get();
	}
	
	/*obtiene el vuelo operado por la compañía dada que tiene una hora planificada anterior. 
	En caso de igualdad, desempatar por el orden natural.*/
	public Vuelo getPrimerVueloCompañia(String compañia) {
		Comparator<Vuelo> cmp = Comparator.comparing(Vuelo::getHoraPlanificada).thenComparing(Comparator.naturalOrder());
		
		return vuelos.stream()
					 .filter(x->x.getCompania().equals(compañia))
					 .min(cmp)
					 .get();
	}
	/*obtiene los n primeros destinos, ordenados por fecha y hora de salida.*/
	public List<String> getPrimerosDestinos(Integer n){
		return vuelos.stream()			
					 .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
					 .sorted(Comparator.comparing(Vuelo::getFecha).thenComparing(Vuelo::getHoraPlanificada))
					 .limit(n)
					 .map(Vuelo::getCiudad)
					 .collect(Collectors.toList());
	}
	
	/*desvía a nuevaCiudad todos los vuelos de salida que tienen como destino ciudad 
	 * (por ejemplo, debido a condiciones meteorológicas adversas).*/
	public void desviaVuelosCiudad(String ciudad, String nuevaCiudad) {
		vuelos.stream()
			  .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
			  .filter(x->x.getCiudad().equals(ciudad))
			  .forEach(x->x.setCiudad(nuevaCiudad));
	}
	
	/*obtiene un Map que relaciona las fechas con los vuelos que salen o llegan en esa fecha*/
	public Map<String, List<Vuelo>> getVuelosPorCiudad(){
		return vuelos.stream()
					 .collect(Collectors.groupingBy(Vuelo::getCiudad));
	}
	
	/*obtiene un Map que relaciona las fechas con los vuelos que salen o llegan en esa fecha*/
	public Map<LocalDate, List<Vuelo>> getVuelosPorFecha(){
		return vuelos.stream()
					 .collect(Collectors.groupingBy(Vuelo::getFecha));
	}
		
	/*obtiene un Map que relaciona las compañías con el conjunto de modelos de avión de la compañía*/
	public Map<String, Set<String>> getModelosPorCompañia(){
		return vuelos.stream()	
					 .collect(Collectors.groupingBy(Vuelo::getCompania, 
							 Collectors.mapping(Vuelo::getModeloAvion, Collectors.toSet())));
	}
	
	/*obtiene un Map que relaciona las ciudades con el número de vuelos que tienen por origen o destino dicha ciudad.*/
	public Map<String, Long> getNumeroVuelosPorCiudad(){
		return vuelos.stream()	
					 .collect(Collectors.groupingBy(Vuelo::getCiudad, 
							 Collectors.counting()));
	}
	
	/*obtiene un Map que relaciona las compañías con el número de vuelos con retraso de la compañía.*/
	public Map<String, Integer> getNumeroVuelosConRetrasoPorCompañia(){
		return vuelos.stream()
					 .collect(Collectors.groupingBy(Vuelo::getCompania, 
							 Collectors.filtering(x->x.diferenciaEnMinutos()<0, Collectors.collectingAndThen(Collectors.counting(), x->x.intValue()))));
	}
	
	/*obtiene un SortedMap que relaciona cada hora del día con el número de salidas que se producen en dicha hora,
	en orden creciente de hora*/
	public SortedMap<Integer, Long> getNumeroSalidasPorHora(){
		return vuelos.stream()
					 .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
					 .collect(Collectors.groupingBy(Vuelo::getHoraPlanificadaHora, 
							 TreeMap::new, 
							 Collectors.counting()));
	}
	
	/*obtiene un SortedMap que relaciona cada hora del día con el número de salidas que se producen en dicha hora,
	en orden DECRECIENTE de hora*/
	public SortedMap<Integer, Long> getNumeroSalidasPorHoraDecreciente(){
		return vuelos.stream()
					 .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
					 .collect(Collectors.groupingBy(Vuelo::getHoraPlanificadaHora, 
							 ()->new TreeMap<Integer, Long>(Comparator.reverseOrder()), 
							 Collectors.counting()));
	}
	
	/*obtiene un Map que relaciona las compañías con el retraso acumulado de todos los vuelos de la compañía*/
	public Map<String, Long> getRetrasoAcumuladoPorCompañia(){
		return vuelos.stream()
					 .filter(x->x.diferenciaEnMinutos()>0)
					 .collect(Collectors.groupingBy(Vuelo::getCompania,
							 Collectors.summingLong(Vuelo::diferenciaEnMinutos)));
	}
	
	/*obtiene un Map que relaciona las compañías con el retraso medio de los vuelos de la compañía.*/
	public Map<String, Double> getRetrasoMedioPorCompañia(){
		return vuelos.stream()
					 .filter(x->x.diferenciaEnMinutos()>0)
					 .collect(Collectors.groupingBy(Vuelo::getCompania, 
							 Collectors.averagingLong(Vuelo::diferenciaEnMinutos)));
	}
	
	/*obtiene el vuelo con más minutos de retraso de cada compañía*/
	public Map<String, Vuelo> getVueloMasRetrasoPorCompañia(){
		return vuelos.stream()
					 .filter(x->x.diferenciaEnMinutos()>0)
					 .collect(Collectors.groupingBy(Vuelo::getCompania, 
							 Collectors.collectingAndThen(
									 Collectors.minBy(Comparator.comparing(Vuelo::diferenciaEnMinutos)), o->o.get()))); // si me dice que devuelva null o x si falla se hace 
																									//   o->o.orElse(null, x, etc)))));
	}
	
	/*obtiene el vuelo que sale a una hora más temprana con destino a cada ciudad.*/
	public Map<String, Vuelo> getVueloMasTempranoPorCiudad(){
		return vuelos.stream()
					 .filter(x->x.diferenciaEnMinutos()>0)
					 .filter(x->x.getDireccion().equals(DireccionVuelo.IN))
					 .collect(Collectors.groupingBy(Vuelo::getCiudad, 
							 (Collectors.collectingAndThen(
									 Collectors.minBy(Comparator.comparing(Vuelo::getHoraPlanificada)), o->o.get()))));
	}
	
	/*obtiene el porcentaje de vuelos con retraso de cada compañía respecto al número total de vuelos con retraso*/
	public Map<String, Double> getPorcentajeVuelosConRetrasoPorCompañia(){
		Long totalRetraso = vuelos.stream()
								  .filter(x->x.diferenciaEnMinutos()<0)
								  .count();
		
		
		return vuelos.stream()	
					 .filter(x->x.diferenciaEnMinutos()<0)
					 .collect(Collectors.groupingBy(Vuelo::getCompania, 
							 Collectors.collectingAndThen(Collectors.counting(), 
									 x->x/totalRetraso*100d)));
					 
	}
	
	/*obtiene las n primeras ciudades de los vuelos de cada fecha, 
	ordenadas por la hora planificada de salida.*/
	public Map<LocalDate, List<String>> getNPrimerasCiudadesPorFecha(Integer n){
		return vuelos.stream()
			  .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
			  .collect(Collectors.groupingBy(Vuelo::getFecha,  
							  Collectors.collectingAndThen(Collectors.toList(), 
									  x->getNPrimerasOrdenadas(x, n))));											// ESTE CAE SI O SI 
	}
	
	public List<String> getNPrimerasOrdenadas(List<Vuelo>vuelos, Integer n) {
		return vuelos.stream()
					   .sorted(Comparator.comparing(Vuelo::getHoraPlanificada))
					   .limit(n)
					   .map(Vuelo::getCiudad)
					   .collect(Collectors.toList());
	}
	
	/*obtiene el número de destinos diferentes a los que hay vuelos en cada mes del año.*/
	public Map<Month, Integer> getNumeroDestinosDiferentesPorMes(){
		return vuelos.stream()
					 .filter(x->x.getDireccion().equals(DireccionVuelo.OUT))
					 .collect(Collectors.groupingBy(x->x.getFecha().getMonth(), 
							 Collectors.mapping(Vuelo::getCiudad, 
									 Collectors.collectingAndThen(Collectors.toSet(), 
											 x->x.size()))));
	}
	
	/*obtiene la  SEGUNDA compañía con más operaciones de vuelo del aeropuerto.*/
	public String getSEGUNDACompañiaMasOperaciones() {
		Map<String, Long> mapa = vuelos.stream()	
				.collect(Collectors.groupingBy(Vuelo::getCompania, 
						Collectors.counting()));
		
		
		Comparator<Map.Entry<String, Long>> cmp = Comparator.comparing(Entry::getValue);
		return mapa.entrySet().stream()
				.sorted(cmp.reversed())
				.skip(1)
				.findFirst()
				.get()
				.getKey();
				
	}
}
