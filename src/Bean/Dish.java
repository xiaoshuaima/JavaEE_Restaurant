package Bean;
import java.math.BigDecimal;
public class Dish {
	private BigDecimal DISHNO;
	private String DISHNAME;
	private String DESCRIPTION;
	private String IMG;
	private BigDecimal  PRICE;
	
public BigDecimal getDISHNO() {
		return DISHNO;
	}
	public void setDISHNO(BigDecimal dISHNO) {
		DISHNO = dISHNO;
	}
	public String getDISHNAME() {
		return DISHNAME;
	}
	public void setDISHNAME(String dISHNAME) {
		DISHNAME = dISHNAME;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getIMG() {
		return IMG;
	}
	public void setIMG(String iMG) {
		IMG = iMG;
	}
	public BigDecimal getPRICE() {
		return PRICE;
	}
	public void setPRICE(BigDecimal pRICE) {
		PRICE = pRICE;
	}

}
