package a2vfclient;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Classe définissant les informations d'un ticket
 *
 * @author Thierry Baribaud
 * @version 1.0.6
 */
public class TicketInfos {

    /**
     * Nom de l'entreprise Vinci Facilities
     */
    private String companyName;

    /**
     * Référence à l'origine du ticket
     */
    private String ticketOriginCode;

    /**
     * Identifiant du contact externe
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contactExternalId;

    /**
     * Adresse électronique du contact
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contactEmailAddress;

    /**
     * Nom du contact
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contactLastName;

    /**
     * Prénom du contact
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contactFirstName;

    /**
     * Téléphone portable du contact
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contactMobilePhone;

    /**
     * Titre de la raison d'appel
     */
    private String ticketSubject;

    /**
     * Raison de l'appel
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ticketRemarks;

    /**
     * Identifiant du type de problème
     */
    private String problemTypeCode;

    /**
     * Priorité du ticket
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ticketPriority;

    /**
     * Référence externe au ticket
     */
    private String ticketExternalId;

    /**
     * Identifiant du site
     */
    private String siteId;

    /**
     * Type de ticket
     */
    private String ticketType;

    /**
     * Contact supplémentaire
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String additionalContact;

    /**
     * Urgence des travaux
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workImpactUrgency;

    /**
     * Impact sur l'environnement
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workEnvironmentImpactUrgency;

    /**
     * Date de création du ticket
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ticketCreationDate;

    /**
     * Indicateur d'astreinte
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int dutyFlag;

    /**
     * Etat du ticket
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ticketBusinessStatus;

    /**
     * Constructeur de la classe TicketInfos
     */
    public TicketInfos() {
        ticketOriginCode = "INTX";
        problemTypeCode = "S-AUX";
        ticketPriority = "LOW";
        ticketType = "1";
        dutyFlag = 1;
        ticketBusinessStatus = "10101";
    }

    /**
     * @return retourne le nom de l'entreprise
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName définit le nom de l'entreprise
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return retourne l'origine du ticket
     */
    public String getTicketOriginCode() {
        return ticketOriginCode;
    }

    /**
     * @param ticketOriginCode définit l'origine du ticket
     */
    public void setTicketOriginCode(String ticketOriginCode) {
        this.ticketOriginCode = ticketOriginCode;
    }

    /**
     * @return l'identifiant du contact externe
     */
    public String getContactExternalId() {
        return contactExternalId;
    }

    /**
     * @param contactExternalId définit l'identifiant du contact externe
     */
    public void setContactExternalId(String contactExternalId) {
        this.contactExternalId = contactExternalId;
    }

    /**
     * @return l'adresse électronique du contact
     */
    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    /**
     * @param contactEmailAddress définit l'adresse électronique du contact
     */
    public void setContactEmailAddress(String contactEmailAddress) {
        this.contactEmailAddress = contactEmailAddress;
    }

    /**
     * @return le nom du contact
     */
    public String getContactLastName() {
        return contactLastName;
    }

    /**
     * @param contactLastName définit le nom du contact
     */
    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    /**
     * @return le prénom du contact
     */
    public String getContactFirstName() {
        return contactFirstName;
    }

    /**
     * @param contactFirstName définit le prénom du contact
     */
    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    /**
     * @return le téléphone portable du contact
     */
    public String getContactMobilePhone() {
        return contactMobilePhone;
    }

    /**
     * @param contactMobilePhone définit le téléphone portable du contact
     */
    public void setContactMobilePhone(String contactMobilePhone) {
        this.contactMobilePhone = contactMobilePhone;
    }

    /**
     * @return le titre de la raison d'appel
     */
    public String getTicketSubject() {
        return ticketSubject;
    }

    /**
     * @param ticketSubject définit le titre de la raison d'appel
     */
    public void setTicketSubject(String ticketSubject) {
        this.ticketSubject = ticketSubject;
    }

    /**
     * @return la raison de l'appel
     */
    public String getTicketRemarks() {
        return ticketRemarks;
    }

    /**
     * @param ticketRemarks définit la raison de l'appel
     */
    public void setTicketRemarks(String ticketRemarks) {
        this.ticketRemarks = ticketRemarks;
    }

    /**
     * @return l'identifiant du type de problème
     */
    public String getProblemTypeCode() {
        return problemTypeCode;
    }

    /**
     * @param problemTypeCode définit l'identifiant du type de problème
     */
    public void setProblemTypeCode(String problemTypeCode) {
        this.problemTypeCode = problemTypeCode;
    }

    /**
     * @return retourne la priorité du ticket
     */
    public String getTicketPriority() {
        return ticketPriority;
    }

    /**
     * @param ticketPriority définit la priorité du ticket
     */
    public void setTicketPriority(String ticketPriority) {
        this.ticketPriority = ticketPriority;
    }

    /**
     * @return retourne la référence externe du ticket
     */
    public String getTicketExternalId() {
        return ticketExternalId;
    }

    /**
     * @param ticketExternalId définit la référence externe du ticket
     */
    public void setTicketExternalId(String ticketExternalId) {
        this.ticketExternalId = ticketExternalId;
    }

    /**
     * @return l'identifiant du site
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId définit l'identifiant du site
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return le type de ticket
     */
    public String getTicketType() {
        return ticketType;
    }

    /**
     * @param ticketType définit le type de ticket
     */
    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    /**
     * @return le contact supplémentaire
     */
    public String getAdditionalContact() {
        return additionalContact;
    }

    /**
     * @param additionalContact définit le contact supplémentaire
     */
    public void setAdditionalContact(String additionalContact) {
        this.additionalContact = additionalContact;
    }

    /**
     * @return l'urgence des travaux
     */
    public String getWorkImpactUrgency() {
        return workImpactUrgency;
    }

    /**
     * @param workImpactUrgency définit l'urgence des travaux
     */
    public void setWorkImpactUrgency(String workImpactUrgency) {
        this.workImpactUrgency = workImpactUrgency;
    }

    /**
     * @return l'impact sur l'environnement
     */
    public String getWorkEnvironmentImpactUrgency() {
        return workEnvironmentImpactUrgency;
    }

    /**
     * @param workEnvironmentImpactUrgency définit l'impact sur l'environnement
     */
    public void setWorkEnvironmentImpactUrgency(String workEnvironmentImpactUrgency) {
        this.workEnvironmentImpactUrgency = workEnvironmentImpactUrgency;
    }

    /**
     * @return la date de création du ticket
     */
    public String getTicketCreationDate() {
        return ticketCreationDate;
    }

    /**
     * @param ticketCreationDate définit la date de création du ticket
     */
    public void setTicketCreationDate(String ticketCreationDate) {
        this.ticketCreationDate = ticketCreationDate;
    }

    /**
     * @return l'indicateur d'astreinte
     */
    public int getDutyFlag() {
        return dutyFlag;
    }

    /**
     * @param dutyFlag définit l'indicateur d'astreinte
     */
    public void setDutyFlag(int dutyFlag) {
        this.dutyFlag = dutyFlag;
    }

    /**
     * @return l'état du ticket
     */
    public String getTicketBusinessStatus() {
        return ticketBusinessStatus;
    }

    /**
     * @param ticketBusinessStatus définit l'état du ticket
     */
    public void setTicketBusinessStatus(String ticketBusinessStatus) {
        this.ticketBusinessStatus = ticketBusinessStatus;
    }

    /**
     * @return les informations du ticket sous forme textuelle
     */
    @Override
    public String toString() {
        return "TicketInfo:{"
                + ", conpanyName:" + getCompanyName()
                + ", ticketOriginCode:" + getTicketOriginCode()
                + ", contactExternalId:" + getContactExternalId()
                + ", contactEmailAddress:" + getContactEmailAddress()
                + ", contactLastNam:" + getContactLastName()
                + ", contactFirstName:" + getContactFirstName()
                + ", callContactMobilePhone:" + getContactMobilePhone()
                + ", ticketSubject:" + getTicketSubject()
                + ", ticketRemarks:" + getTicketRemarks()
                + ", problemTypeCode:" + getProblemTypeCode()
                + ", ticketPriority:" + getTicketPriority()
                + ", ticketExternalId:" + getTicketExternalId()
                + ", siteId:" + getSiteId()
                + ", ticketType:" + getTicketType()
                + ", additionalContact:" + getAdditionalContact()
                + ", workImpactUrgency:" + getWorkImpactUrgency()
                + ", workEnvironmentImpactUrgency:" + getWorkEnvironmentImpactUrgency()
                + ", ticketCreationDate:" + getTicketCreationDate()
                + ", dutyFlag:" + getDutyFlag()
                + ", ticketBusinessStatus:" + getTicketBusinessStatus()
                + "}";
    }

}
