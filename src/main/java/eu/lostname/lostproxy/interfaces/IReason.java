package eu.lostname.lostproxy.interfaces;


import eu.lostname.lostproxy.enums.EReasonType;

public class IReason {

    private int _id;
    private String name;
    private EReasonType eReasonType;

    public IReason(int _id, String name, EReasonType eReasonType) {
        this._id = _id;
        this.name = name;
        this.eReasonType = eReasonType;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EReasonType getReasonType() {
        return eReasonType;
    }

    public void setReasonType(EReasonType eReasonType) {
        this.eReasonType = eReasonType;
    }
}
