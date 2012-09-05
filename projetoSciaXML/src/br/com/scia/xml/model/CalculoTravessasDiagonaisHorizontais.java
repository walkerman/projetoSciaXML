package br.com.scia.xml.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.scia.xml.dao.RepositorioPecas;
import br.com.scia.xml.entity.view.Peca;
import br.com.scia.xml.entity.view.SumarioDados;
import br.com.scia.xml.entity.xml.Coordenada;
import br.com.scia.xml.util.CoordenadaSorterZ;


public class CalculoTravessasDiagonaisHorizontais {

	private SumarioDados sumarioDados;
	private Integer identificadorPecas;
	
	public CalculoTravessasDiagonaisHorizontais() {
		this.sumarioDados = Calculo.dados;
		
		if (this.sumarioDados != null){
			this.identificadorPecas = this.sumarioDados.getPecasFinais().size()+1;
		}
	}
	
	public void realizarCalculo(){
		List<Peca> travessasReplicadas = RepositorioPecas.listaTravessasReplicadas;
		Double zInicial = getMenorZ(travessasReplicadas);
		
		if (travessasReplicadas != null && travessasReplicadas.size() > 0){						
			ArrayList<Peca> travessasMesmoZ = new ArrayList<Peca>();
			for (Peca travessaReferencia : travessasReplicadas) {
				if (travessaReferencia.getNoInicial().getZ().doubleValue() == zInicial.doubleValue() ||
					travessaReferencia.getNoFinal().getZ().doubleValue() == zInicial.doubleValue()){
					travessasMesmoZ.add(travessaReferencia);
				}
			}
			
			List<String> idAdicionados = new ArrayList<String>();
			ArrayList<ArrayList<Peca>> listaDeListas = new ArrayList<ArrayList<Peca>>();
			for (Peca pecaReferencia : travessasMesmoZ) {
				if (!idAdicionados.contains(pecaReferencia.getId())){
				
					ArrayList<Peca> travessasMesmoY = null;
					
					if (pecaReferencia.getNoInicial().getX().doubleValue() == pecaReferencia.getNoFinal().getX().doubleValue()){
						travessasMesmoY = new ArrayList<Peca>();
						travessasMesmoY.add(pecaReferencia);
						idAdicionados.add(pecaReferencia.getId());
					}				
						
					for (Peca travessa : travessasMesmoZ) {
						if ((pecaReferencia.getNoInicial().getY().doubleValue() == travessa.getNoFinal().getY().doubleValue()) &&
							(pecaReferencia.getNoFinal().getY().doubleValue() == travessa.getNoInicial().getY().doubleValue())){
							
							if (travessasMesmoY != null){
								travessasMesmoY.add(travessa);
								idAdicionados.add(travessa.getId());
							}
						}
					}
					
					if (travessasMesmoY != null){
						listaDeListas.add(travessasMesmoY);
						System.out.println(travessasMesmoY.size());
					}
				}
			}
			
			System.out.println(listaDeListas.size());
			
//			for (ArrayList<Peca> lista : listaDeListas) {
//				boolean isNoFinal = false;
//				for (int i = 0; i < lista.size(); i++) {
//					if (i > 0){
//						Peca peca = lista.get(i);
//						Peca pecaAnterior = lista.get(i-1);
//							
//						Peca novaTravessaDiagonal = new Peca();
//						novaTravessaDiagonal.setId(String.valueOf(identificadorPecas));
//						novaTravessaDiagonal.setName(SciaXMLConstantes.INDEXADOR_PECA + String.valueOf(identificadorPecas++));
//						novaTravessaDiagonal.setTipo(SciaXMLConstantes.KIDI1215);
//						
//						if (isNoFinal){
//							novaTravessaDiagonal.setNoInicial(pecaAnterior.getNoFinal());
//							novaTravessaDiagonal.setNoFinal(peca.getNoInicial());
//							isNoFinal = true;
//						}else{
//							novaTravessaDiagonal.setNoInicial(pecaAnterior.getNoInicial());
//							novaTravessaDiagonal.setNoFinal(peca.getNoFinal());
//							isNoFinal = false;
//						}
//						
//						this.sumarioDados.getPecasFinais().add(novaTravessaDiagonal);
//					}
////				}
//			}
		}
	}
	
	
	// M�todo respons�vel por tirar as duplicidades de n�s em X
	private Double getMenorZ (List<Peca> pecas){
		Double retorno = null;
		Set<Double> valoresZ = new HashSet<Double>();
		
		List<Coordenada> coordTemp = new ArrayList<Coordenada>();
		for (Peca p : pecas) {
			coordTemp.add(p.getNoInicial());
		}
		
		// Removendo os itens repetidos
		for (Coordenada coordenada : coordTemp) {
			valoresZ.add(coordenada.getZ());
		}
		
		if (valoresZ != null && valoresZ.size() > 0){
			ArrayList<Coordenada> coordenadas = new ArrayList<Coordenada>();
			for (Double valor : valoresZ) {
				Coordenada c = new Coordenada();
				c.setZ(valor);
				coordenadas.add(c);
			} 
			
			Collections.sort(coordenadas,new CoordenadaSorterZ());
			
			if (coordenadas!= null && coordenadas.size() > 0)
				retorno = coordenadas.get(0).getZ();
		}
		
		return retorno;
	}
	
}