package demandes;

import java.util.Date;

public class Demand {
    private int id;
    private String typeDemandeID;
    private int etudiantID;
    private String etudiantNom;
    private Date dateSubmission;
    private Date dateReponse;
    private String details;
    private boolean isValidated;

    public Demand(int id, String typeDemandeID, int etudiantID, Date dateSubmission, Date dateReponse, String details, boolean isValidated, String etudiantNom) {
        this.id = id;
        this.typeDemandeID = typeDemandeID;
        this.etudiantID = etudiantID;
        this.etudiantNom = etudiantNom;
        this.dateSubmission = dateSubmission;
        this.dateReponse = dateReponse;
        this.details = details;
        this.isValidated = isValidated;
    }

    // Constructors with default values for optional parameters
    public Demand(String typeDemandeID, int etudiantID, Date dateSubmission, String details ,String etudiantNom) {
        this(0, typeDemandeID, etudiantID, dateSubmission, null, details, false, etudiantNom);
    }

    public Demand() {
        // Default constructor
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeDemandeID() {
        return typeDemandeID;
    }

    public void setTypeDemandeID(String typeDemandeID) {
        this.typeDemandeID = typeDemandeID;
    }

    public int getEtudiantID() {
        return etudiantID;
    }

    public void setEtudiantID(int etudiantID) {
        this.etudiantID = etudiantID;
    }
    public String getEtudiantNom() {
        return etudiantNom;
    }

    public void setEtudiantNom(String etudiantNom) {
        this.etudiantNom = etudiantNom;
    }

    public Date getDateSubmission() {
        return dateSubmission;
    }

    public void setDateSubmission(Date dateSubmission) {
        this.dateSubmission = dateSubmission;
    }

    public Date getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Date dateReponse) {
        this.dateReponse = dateReponse;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "id=" + id +
                ", typeDemandeID=" + typeDemandeID +
                ", etudiantID=" + etudiantID +
                ", dateSubmission=" + dateSubmission +
                ", dateReponse=" + dateReponse +
                ", details='" + details + '\'' +
                ", isValidated=" + isValidated +
                '}';
    }
}
