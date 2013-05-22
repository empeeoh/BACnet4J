package com.serotonin.bacnet4j.npdu.mstp;

import com.serotonin.bacnet4j.npdu.NetworkIdentifier;

public class MstpNetworkIdentifier extends NetworkIdentifier {
    private final String commPortId;

    public MstpNetworkIdentifier(String commPortId) {
        this.commPortId = commPortId;
    }

    public String getCommPortId() {
        return commPortId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((commPortId == null) ? 0 : commPortId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MstpNetworkIdentifier other = (MstpNetworkIdentifier) obj;
        if (commPortId == null) {
            if (other.commPortId != null)
                return false;
        }
        else if (!commPortId.equals(other.commPortId))
            return false;
        return true;
    }
}
