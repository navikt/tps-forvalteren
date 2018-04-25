
package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Responsfeltene fra S051 - FS03-ADRSNAVN-ADRSDATA-O
 * 
 * <p>Java class for AdresseDataFraTpsS051 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdresseDataFraTpsS051">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="antallForekomster" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="adrData" type="{http://www.rtv.no/NamespaceTPS}adresseData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdresseDataFraTpsS051", namespace = "http://www.rtv.no/NamespaceTPS", propOrder = {
    "antallForekomster",
    "adrData"
})
public class AdresseDataFraTpsS051 {

    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
    protected String antallForekomster;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
    protected List<AdresseData> adrData;

    /**
     * Gets the value of the antallForekomster property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntallForekomster() {
        return antallForekomster;
    }

    /**
     * Sets the value of the antallForekomster property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntallForekomster(String value) {
        this.antallForekomster = value;
    }

    /**
     * Gets the value of the adrData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adrData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdrData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdresseData }
     * 
     * 
     */
    public List<AdresseData> getAdrData() {
        if (adrData == null) {
            adrData = new ArrayList<AdresseData>();
        }
        return this.adrData;
    }

}
