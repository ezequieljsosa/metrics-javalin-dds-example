package ar.edu.utn.dds.k3003;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferDTO {
	
	private String src;
	private String dst;
	private Double amount;

}
