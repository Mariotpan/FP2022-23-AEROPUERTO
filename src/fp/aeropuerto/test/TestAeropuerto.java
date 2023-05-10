package fp.aeropuerto.test;

import fp.aeropuerto.FactoriaVuelos;
import fp.aeropuerto.aeropuerto;

public class TestAeropuerto {

	public static void main(String[] args) {
		aeropuerto sanPablo = FactoriaVuelos.leeVuelos("data/SVQ.csv");
		
		Long retrasoIberia = sanPablo.getMinutosRetrasoAcumuladosCompañia("Iberia");
		System.out.println("El retraso de Iberia es : "+retrasoIberia);
		
		/**/
		testGetMinutosRetrasoAcumuladosCompañia(sanPablo, "Iberia");
		testGetMinutosRetrasoAcumuladosCompañia(sanPablo, "COMPANIA INVENTADA");
		/**/
	}
	
	public static void testGetMinutosRetrasoAcumuladosCompañia(fp.aeropuerto.aeropuerto aeropuerto, String compania) {
		Long retrasoIberia = aeropuerto.getMinutosRetrasoAcumuladosCompañia("Iberia");
		System.out.println("El retraso de "+compania+" es "+retrasoIberia);
	}
}
