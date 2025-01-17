package br.com.scia.xml.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.scia.xml.dao.RepositorioPecas;
import br.com.scia.xml.entity.view.Coordenada;
import br.com.scia.xml.entity.view.Peca;
import br.com.scia.xml.entity.view.SumarioDados;
import br.com.scia.xml.util.CoordenadaSorterZ;
import br.com.scia.xml.util.Identificadores;
import br.com.scia.xml.util.SciaXMLConstantes;

public class CalculoTravessas {

	private SumarioDados sumarioDados;
	
	public CalculoTravessas() {
		this.sumarioDados = Calculo.dados;
		
		if (this.sumarioDados != null){
		}
	}
	
	public void replicarTravessas(){
		if (RepositorioPecas.listaRosaceas != null && RepositorioPecas.listaRosaceas.size() > 0){
			if (RepositorioPecas.listaTravessas != null && RepositorioPecas.listaTravessas.size() > 0){
			
				List<Peca> rosaceas = RepositorioPecas.listaRosaceas;
				List<Peca> travessas = RepositorioPecas.listaTravessas;
				
				List<Coordenada> coordenadasRosaceas = new ArrayList<Coordenada>();
				for (Peca rosacea : rosaceas) {
					Coordenada c = rosacea.getNoInicial();
					coordenadasRosaceas.add(c);
				}
				
				List<Coordenada> alturasRosaceas = getCoordenadasZ(coordenadasRosaceas);
				Collections.sort(alturasRosaceas, new CoordenadaSorterZ());
				
				//System.out.println(alturasRosaceas);
				
				double contador = 0.0;
				double entreKITRV = Double.parseDouble(this.sumarioDados.getEspacamentoEntreTravessas())/100.0;
				
				Coordenada coordenadaAnterior = null;
				for (int i = 0; i < alturasRosaceas.size(); i++) {
					contador = Math.round(contador*100)/100.0;
					
					Coordenada coordenada = alturasRosaceas.get(i);
					
					if (i == 0 || contador == entreKITRV || i == alturasRosaceas.size()-1){
												
						if (i > 0){
							coordenadaAnterior = alturasRosaceas.get(i-1);
							contador = ( coordenada.getZ() - coordenadaAnterior.getZ());
						}else{
							contador = 0;
						}
						
						for (Peca travessa : travessas) {
							
							Coordenada coordenada1 = new Coordenada();
							Coordenada coordenada2 = new Coordenada();
							
							Coordenada coordenadaInicialTravessa = travessa.getNoInicial();
							Coordenada coordenadaFinalTravessa = travessa.getNoFinal();
							
							coordenada1.setId(Identificadores.getIdentificadorNo().toString());
							coordenada1.setName(SciaXMLConstantes.INDEXADOR_NO + String.valueOf(coordenada1.getId()));					
							coordenada1.setX(coordenadaInicialTravessa.getX());
							coordenada1.setY(coordenadaInicialTravessa.getY());
							
							if (coordenadaAnterior == null || (coordenadaAnterior != null && i == alturasRosaceas.size()-1))
								coordenada1.setZ(coordenada.getZ() + SciaXMLConstantes.ALTURA_ROSACEA / 2);
							else
								coordenada1.setZ(coordenadaAnterior.getZ() + SciaXMLConstantes.ALTURA_ROSACEA / 2);
							
							coordenada2.setId(Identificadores.getIdentificadorNo().toString());
							coordenada2.setName(SciaXMLConstantes.INDEXADOR_NO + String.valueOf(coordenada2.getId()));					
							coordenada2.setX(coordenadaFinalTravessa.getX());
							coordenada2.setY(coordenadaFinalTravessa.getY());

							if (coordenadaAnterior == null || (coordenadaAnterior != null && i == alturasRosaceas.size()-1))
								coordenada2.setZ(coordenada.getZ() + SciaXMLConstantes.ALTURA_ROSACEA / 2);
							else
								coordenada2.setZ(coordenadaAnterior.getZ() + SciaXMLConstantes.ALTURA_ROSACEA / 2);
							
							Peca novaTravessa = new Peca();
							novaTravessa.setId(String.valueOf(Identificadores.getIdentificarPecas().toString()));
							novaTravessa.setName(SciaXMLConstantes.INDEXADOR_PECA + String.valueOf(novaTravessa.getId()));
							novaTravessa.setTipo(travessa.getTipo());
							novaTravessa.setNoInicial(coordenada1);
							novaTravessa.setNoFinal(coordenada2);
							
							this.sumarioDados.getListaDeNos().add(coordenada1);
							this.sumarioDados.getListaDeNos().add(coordenada2);
							this.sumarioDados.getPecasFinais().add(novaTravessa);
							
							RepositorioPecas.listaTravessasReplicadas.add(novaTravessa);
						}
					}else{
						coordenadaAnterior = alturasRosaceas.get(i-1);
						contador += ( coordenada.getZ() - coordenadaAnterior.getZ());
					}
				}
			}
		}
	}
	
	// M�todo respons�vel por tirar as duplicidades de n�s em Z
	private List<Coordenada> getCoordenadasZ (List<Coordenada> coordenadas){
		List<Coordenada> retorno = null;
		Set<Double> valoresZ = new HashSet<Double>();
		
		// Removendo os itens repetidos
		for (Coordenada coordenada : coordenadas) {
			if (coordenada != null)
				valoresZ.add(coordenada.getZ());
		}
		
		if (valoresZ != null && valoresZ.size() > 0){
			retorno = new ArrayList<Coordenada>();
			for (Double valor : valoresZ) {
				Coordenada c = new Coordenada();
				c.setZ(valor);
				retorno.add(c);
			} 
		}
		
		return retorno;
	}
	
}
