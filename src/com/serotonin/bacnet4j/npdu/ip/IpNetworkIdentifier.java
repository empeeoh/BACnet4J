package com.serotonin.bacnet4j.npdu.ip;

import com.serotonin.bacnet4j.npdu.NetworkIdentifier;

public class IpNetworkIdentifier extends NetworkIdentifier {
    private final int port;
    private final String localBindAddress;

    public IpNetworkIdentifier(int port, String localBindAddress) {
        this.port = port;
        this.localBindAddress = localBindAddress;
    }

    public int getPort() {
        return port;
    }

    public String getLocalBindAddress() {
        return localBindAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((localBindAddress == null) ? 0 : localBindAddress.hashCode());
        result = prime * result + port;
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
        IpNetworkIdentifier other = (IpNetworkIdentifier) obj;
        if (localBindAddress == null) {
            if (other.localBindAddress != null)
                return false;
        }
        else if (!localBindAddress.equals(other.localBindAddress))
            return false;
        if (port != other.port)
            return false;
        return true;
    }
}
