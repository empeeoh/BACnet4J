package org.free.bacnet4j.util;

public class IpAddressUtils{

	public static boolean ipWhiteListCheck(String allowedIp, String remoteIp)throws IpWhiteListException{
		String remoteIpParts[] = remoteIp.split("\\.");
		if(remoteIpParts.length != 4)
			throw new IpWhiteListException((new StringBuilder()).append("Invalid remote IP address: ").append(remoteIp).toString());
		else
			return ipWhiteListCheckImpl(allowedIp, remoteIp, remoteIpParts);
	}

	public static boolean ipWhiteListCheck(String allowedIps[], String remoteIp)
													throws IpWhiteListException{
		String remoteIpParts[] = remoteIp.split("\\.");
		if(remoteIpParts.length != 4)
			throw new IpWhiteListException((new StringBuilder()).append("Invalid remote IP address: ").append(remoteIp).toString());
		for(int i = 0; i < allowedIps.length; i++)
			if(ipWhiteListCheckImpl(allowedIps[i], remoteIp, remoteIpParts))
				return true;
		return false;
	}

	private static boolean ipWhiteListCheckImpl(String allowedIp, String remoteIp, String remoteIpParts[])
												   throws IpWhiteListException {
		String allowedIpParts[] = allowedIp.split("\\.");
		if(allowedIpParts.length != 4)
			throw new IpWhiteListException((new StringBuilder()).append("Invalid allowed IP address: ").append(allowedIp).toString());
		else
			return validateIpPart(allowedIpParts[0], remoteIpParts[0], allowedIp, remoteIp) 
				&& validateIpPart(allowedIpParts[1], remoteIpParts[1], allowedIp, remoteIp)
				&& validateIpPart(allowedIpParts[2], remoteIpParts[2], allowedIp, remoteIp)
				&& validateIpPart(allowedIpParts[3], remoteIpParts[3], allowedIp, remoteIp);
		}

	private static boolean validateIpPart(String allowed, String remote, String allowedIp, String remoteIp)
													throws IpWhiteListException{
		if("*".equals(allowed))
			return true;
		int dash = allowed.indexOf('-');
		int from;
		int to;
		int rem;
		try{
			if(dash == -1)
				return Integer.parseInt(allowed) == Integer.parseInt(remote);
		} catch(NumberFormatException e){
			throw new IpWhiteListException((new StringBuilder())
									  .append("Integer parsing error. allowed=")
									  .append(allowedIp).append(", remote=")
									  .append(remoteIp).toString());
		}
		from = Integer.parseInt(allowed.substring(0, dash));
		to = Integer.parseInt(allowed.substring(dash + 1));
		rem = Integer.parseInt(remote);
		return from <= rem && rem <= to;
	}

	public static String checkIpMask(String ip){
		String ipParts[] = ip.split("\\.");
		if(ipParts.length != 4)
			return "IP address must have 4 parts";
		String message = checkIpMaskPart(ipParts[0]);
		if(message != null)
			return message;
		message = checkIpMaskPart(ipParts[1]);
		if(message != null)
			return message;
		message = checkIpMaskPart(ipParts[2]);
		if(message != null)
			return message;
		message = checkIpMaskPart(ipParts[3]);
		if(message != null)
			return message;
		else
			return null;
	}

	private static String checkIpMaskPart(String part){
		int dash;
		if("*".equals(part))
			return null;
		dash = part.indexOf('-');
		if(dash == -1){
			int value = Integer.parseInt(part);
			if(value < 0 || value > 255)
				return (new StringBuilder()).append("Value out of range in '")
										   .append(part).append("'").toString();
			//break MISSING_BLOCK_LABEL_219;
		}
		int from;
		from = Integer.parseInt(part.substring(0, dash));
		if(from < 0 || from > 255)
			return (new StringBuilder()).append("'From' value out of range in '").append(part).append("'").toString();
		int to;
		try{
			to = Integer.parseInt(part.substring(dash + 1));
			if(to < 0 || to > 255)
				return (new StringBuilder()).append("'To' value out of range in '").append(part).append("'").toString();
		}
		catch(NumberFormatException e){
			return (new StringBuilder()).append("Integer parsing error in '").append(part).append("'").toString();
		}
		if(from > to)
			return (new StringBuilder()).append("'From' value is greater than 'To' value in '").append(part).append("'").toString();
		return null;
	}

	public static byte[] toIpAddress(String addr) throws IllegalArgumentException{
		if(addr == null)
			throw new IllegalArgumentException("Invalid address: (null)");
		String parts[] = addr.split("\\.");
		if(parts.length != 4)
			throw new IllegalArgumentException("IP address must have 4 parts");
		byte ip[] = new byte[4];
		for(int i = 0; i < 4; i++)
			try{
				int part = Integer.parseInt(parts[i]);
				if(part < 0 || part > 255)
					throw new IllegalArgumentException((new StringBuilder()).append("Value out of range in '").append(parts[i]).append("'").toString());
				ip[i] = (byte)part;
		}
		catch(NumberFormatException e){
			throw new IllegalArgumentException((new StringBuilder()).append("Integer parsing error in '").append(parts[i]).append("'").toString());
		}
		return ip;
	}

	public static String toIpString(byte b[]) throws IllegalArgumentException{
		if(b.length != 4)
			throw new IllegalArgumentException("IP address must have 4 parts");
		StringBuilder sb = new StringBuilder();
		sb.append(b[0] & 0xff);
		for(int i = 1; i < b.length; i++)
			sb.append('.').append(b[i] & 0xff);
		return sb.toString();
	}
}