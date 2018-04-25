
package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * StatusRespons fra TPS inneholder returstatus, returmelding og en beskrivende melding.
 * 
 * <p>Java class for StatusFraTPS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StatusFraTPS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="returStatus" type="{http://www.rtv.no/NamespaceTPS}TreturStatus"/>
 *         &lt;element name="returMelding" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="utfyllendeMelding" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatusFraTPS", namespace = "http://www.rtv.no/NamespaceTPS", propOrder = {
    "returStatus",
    "returMelding",
    "utfyllendeMelding"
})
public class StatusFraTPS {

    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
    protected String returStatus;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
    protected String returMelding;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
    protected String utfyllendeMelding;

    /**
     * Gets the value of the returStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturStatus() {
        return returStatus;
    }

    /**
     * Sets the value of the returStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturStatus(String value) {
        this.returStatus = value;
    }

    /**
     * Gets the value of the returMelding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturMelding() {
        return returMelding;
    }

    /**
     * Sets the value of the returMelding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturMelding(String value) {
        this.returMelding = value;
    }

    /**
     * Gets the value of the utfyllendeMelding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUtfyllendeMelding() {
        return utfyllendeMelding;
    }

    /**
     * Sets the value of the utfyllendeMelding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUtfyllendeMelding(String value) {
        this.utfyllendeMelding = value;
    }

}
