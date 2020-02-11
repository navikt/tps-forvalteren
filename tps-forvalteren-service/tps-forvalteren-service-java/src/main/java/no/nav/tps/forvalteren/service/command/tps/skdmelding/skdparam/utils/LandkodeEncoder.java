package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import static java.time.LocalDateTime.of;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
public class LandkodeEncoder {

    private static final LocalDateTime START_OF_ERA = of(1800, 1, 1, 0, 0);
    private static final LocalDateTime FORESEEABLE_FUTURE = of(9999, 12, 31, 0, 0);

    private static final Map<String, LandDetails> landkoderMap = new HashMap<>();
    private Random random = new SecureRandom();

    private static final LandDetails DEFAULT = new LandDetails("990", START_OF_ERA, FORESEEABLE_FUTURE);

    static { //NOSONAR
        landkoderMap.put("???", DEFAULT);
        landkoderMap.put("ABW", new LandDetails("657", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("AFG", new LandDetails("404", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("AGO", new LandDetails("204", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("AIA", new LandDetails("660", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ALA", new LandDetails("860", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ALB", new LandDetails("111", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("AND", new LandDetails("114", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ANT", new LandDetails("656", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ARE", new LandDetails("426", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ARG", new LandDetails("705", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ARM", new LandDetails("406", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ASM", new LandDetails("802", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ATF", new LandDetails("628", START_OF_ERA, of(2000, 1, 1, 0, 0)));
        landkoderMap.put("ATG", new LandDetails("603", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("AUS", new LandDetails("805", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("AUT", new LandDetails("153", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("AZE", new LandDetails("407", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BDI", new LandDetails("216", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BEL", new LandDetails("112", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BEN", new LandDetails("229", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BES", new LandDetails("659", of(2011, 12, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("BFA", new LandDetails("393", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BGD", new LandDetails("410", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BGR", new LandDetails("113", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BHR", new LandDetails("409", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BHS", new LandDetails("605", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BIH", new LandDetails("155", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BLM", new LandDetails("687", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BLR", new LandDetails("120", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BLZ", new LandDetails("604", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BMU", new LandDetails("606", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BOL", new LandDetails("710", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BRA", new LandDetails("715", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BRB", new LandDetails("602", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BRN", new LandDetails("416", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BTN", new LandDetails("412", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BVT", new LandDetails("875", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("BWA", new LandDetails("205", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CAF", new LandDetails("337", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CAN", new LandDetails("612", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CCK", new LandDetails("808", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CHE", new LandDetails("141", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CHL", new LandDetails("725", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CHN", new LandDetails("484", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CIV", new LandDetails("239", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CMR", new LandDetails("270", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("COD", new LandDetails("279", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("COG", new LandDetails("278", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("COK", new LandDetails("809", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("COL", new LandDetails("730", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("COM", new LandDetails("220", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CPV", new LandDetails("273", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CRI", new LandDetails("616", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CSK", new LandDetails("142", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CUB", new LandDetails("620", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CUW", new LandDetails("661", of(2011, 12, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("CXR", new LandDetails("807", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CYM", new LandDetails("613", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CYP", new LandDetails("500", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("CZE", new LandDetails("158", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("DDR", new LandDetails("151", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("DEU", new LandDetails("144", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("DJI", new LandDetails("250", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("DMA", new LandDetails("622", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("DNK", new LandDetails("101", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("DOM", new LandDetails("624", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("DZA", new LandDetails("203", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ECU", new LandDetails("735", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("EGY", new LandDetails("249", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ERI", new LandDetails("241", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ESH", new LandDetails("304", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ESP", new LandDetails("137", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("EST", new LandDetails("115", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ETH", new LandDetails("246", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("FIN", new LandDetails("103", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("FJI", new LandDetails("811", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("FLK", new LandDetails("740", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("FRA", new LandDetails("117", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("FRO", new LandDetails("104", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("FSM", new LandDetails("826", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GAB", new LandDetails("254", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GBR", new LandDetails("139", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GEO", new LandDetails("430", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GGY", new LandDetails("162", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GHA", new LandDetails("260", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GIB", new LandDetails("118", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GIN", new LandDetails("264", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GLP", new LandDetails("631", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GMB", new LandDetails("256", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GNB", new LandDetails("266", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GNQ", new LandDetails("235", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GRC", new LandDetails("119", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GRD", new LandDetails("629", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GRL", new LandDetails("102", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GTM", new LandDetails("632", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GUF", new LandDetails("745", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GUM", new LandDetails("817", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("GUY", new LandDetails("720", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("HKG", new LandDetails("436", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("HMD", new LandDetails("870", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("HND", new LandDetails("644", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("HRV", new LandDetails("122", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("HTI", new LandDetails("636", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("HUN", new LandDetails("152", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("IDN", new LandDetails("448", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("IMN", new LandDetails("164", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("IND", new LandDetails("444", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("IOT", new LandDetails("213", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("IRL", new LandDetails("121", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("IRN", new LandDetails("456", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("IRQ", new LandDetails("452", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ISL", new LandDetails("105", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ISR", new LandDetails("460", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ITA", new LandDetails("123", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("JAM", new LandDetails("648", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("JEY", new LandDetails("163", of(2011, 12, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("JOR", new LandDetails("476", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("JPN", new LandDetails("464", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KAZ", new LandDetails("480", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KEN", new LandDetails("276", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KGZ", new LandDetails("502", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KHM", new LandDetails("478", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KIR", new LandDetails("815", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KNA", new LandDetails("677", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KOR", new LandDetails("492", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("KWT", new LandDetails("496", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LAO", new LandDetails("504", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LBN", new LandDetails("508", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LBR", new LandDetails("283", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LBY", new LandDetails("286", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LCA", new LandDetails("678", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LIE", new LandDetails("128", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LKA", new LandDetails("424", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LSO", new LandDetails("281", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LTU", new LandDetails("136", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LUX", new LandDetails("129", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("LVA", new LandDetails("124", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MAC", new LandDetails("510", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MAF", new LandDetails("686", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MAR", new LandDetails("303", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MCO", new LandDetails("130", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MDA", new LandDetails("138", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MDG", new LandDetails("289", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MDV", new LandDetails("513", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MEX", new LandDetails("652", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MHL", new LandDetails("835", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MKD", new LandDetails("156", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MLI", new LandDetails("299", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MLT", new LandDetails("126", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MMR", new LandDetails("420", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MNE", new LandDetails("160", of(2006, 11, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("MNG", new LandDetails("516", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MNP", new LandDetails("840", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MOZ", new LandDetails("319", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MRT", new LandDetails("306", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MSR", new LandDetails("654", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MTQ", new LandDetails("650", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MUS", new LandDetails("307", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MWI", new LandDetails("296", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MYS", new LandDetails("512", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("MYT", new LandDetails("322", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NAM", new LandDetails("308", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NCL", new LandDetails("833", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NER", new LandDetails("309", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NFK", new LandDetails("822", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NGA", new LandDetails("313", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NIC", new LandDetails("664", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NIU", new LandDetails("821", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NLD", new LandDetails("127", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NOR", new LandDetails("000", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NPL", new LandDetails("528", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NRU", new LandDetails("818", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("NZL", new LandDetails("820", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("OMN", new LandDetails("520", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PAK", new LandDetails("534", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PAN", new LandDetails("668", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PCN", new LandDetails("828", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PER", new LandDetails("760", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PHL", new LandDetails("428", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PLW", new LandDetails("839", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PNG", new LandDetails("827", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("POL", new LandDetails("131", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PRI", new LandDetails("685", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PRK", new LandDetails("488", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PRT", new LandDetails("132", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PRY", new LandDetails("755", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PSE", new LandDetails("524", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("PYF", new LandDetails("814", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("QAT", new LandDetails("540", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("REU", new LandDetails("323", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ROU", new LandDetails("133", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("RUS", new LandDetails("140", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("RWA", new LandDetails("329", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SAU", new LandDetails("544", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SCG", new LandDetails("125", of(1994, 1, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("SDN", new LandDetails("356", of(2011, 12, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("SEN", new LandDetails("336", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SGP", new LandDetails("548", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SGS", new LandDetails("865", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SHN", new LandDetails("209", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SJM", new LandDetails("744", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SLB", new LandDetails("806", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SLE", new LandDetails("339", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SLV", new LandDetails("672", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SMR", new LandDetails("134", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SOM", new LandDetails("346", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SPM", new LandDetails("676", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SRB", new LandDetails("159", of(2006, 11, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("SSD", new LandDetails("355", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("STP", new LandDetails("333", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SUN", new LandDetails("135", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SUR", new LandDetails("765", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SVK", new LandDetails("157", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SVN", new LandDetails("146", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SWE", new LandDetails("106", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SWZ", new LandDetails("357", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SXM", new LandDetails("658", of(2011, 12, 1, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("SYC", new LandDetails("338", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("SYR", new LandDetails("564", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TCA", new LandDetails("681", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TCD", new LandDetails("373", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TGO", new LandDetails("376", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("THA", new LandDetails("568", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TJK", new LandDetails("550", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TKL", new LandDetails("829", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TKM", new LandDetails("552", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TLS", new LandDetails("537", of(2002, 5, 20, 0, 0), FORESEEABLE_FUTURE));
        landkoderMap.put("TON", new LandDetails("813", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TTO", new LandDetails("680", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TUN", new LandDetails("379", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TUR", new LandDetails("143", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TUV", new LandDetails("816", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TWN", new LandDetails("432", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("TZA", new LandDetails("369", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("UGA", new LandDetails("386", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("UKR", new LandDetails("148", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("UMI", new LandDetails("819", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("URY", new LandDetails("770", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("USA", new LandDetails("684", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("UZB", new LandDetails("554", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("VAT", new LandDetails("154", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("VCT", new LandDetails("679", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("VEN", new LandDetails("775", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("VGB", new LandDetails("608", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("VIR", new LandDetails("601", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("VNM", new LandDetails("575", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("VUT", new LandDetails("812", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("WAK", new LandDetails("831", START_OF_ERA, of(2001, 1, 1, 0, 0)));
        landkoderMap.put("WLF", new LandDetails("832", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("WSM", new LandDetails("830", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("XXK", new LandDetails("161", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("XXX", new LandDetails("980", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("YEM", new LandDetails("578", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("YUG", new LandDetails("925", of(1944, 1, 1, 0, 0), of(2003, 11, 30, 0, 0)));
        landkoderMap.put("ZAF", new LandDetails("359", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ZMB", new LandDetails("389", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("ZWE", new LandDetails("326", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("349", new LandDetails("349", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("546", new LandDetails("546", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("556", new LandDetails("556", START_OF_ERA, FORESEEABLE_FUTURE));
        landkoderMap.put("669", new LandDetails("669", START_OF_ERA, FORESEEABLE_FUTURE));
    }

    public String getRandomLandTla() {

        String landkode;
        do {
            landkode = (String) landkoderMap.keySet().toArray()[1 + random.nextInt(landkoderMap.size() - 2)];

        } while (landkoderMap.get(landkode).fom.isAfter(START_OF_ERA) ||
                landkoderMap.get(landkode).tom.isBefore(FORESEEABLE_FUTURE) ||
                "NOR".equals(landkode));

        return landkode;
    }

    public String encode(String statsborgerskap) {
        return landkoderMap.getOrDefault(statsborgerskap, DEFAULT).getTpsCode();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class LandDetails {
        private String tpsCode;
        private LocalDateTime fom;
        private LocalDateTime tom;
    }
}
