package gov.va.octo.vistalink;

import gov.va.octo.vistalink.exception.InvalidStationMappingException;
import gov.va.octo.vistalink.exception.VistaLinkConnectionException;
import gov.va.octo.vistalink.model.VistaLinkConnectionX;

public class VistaLinkConnectionCreator {
	
	private static final int CONNECTION_ATTEMPTS = 10;
	private static final int WAIT_BETWEEN_RETRIES = 1000;
	
	
	/**
	 * return a VistaLinkConnectionX containing a vista link connection
	 * 
	 * @param stationNo
	 * @param duz
	 * @return
	 * @throws VistaLinkConnectionException
	 * @throws InvalidStationMappingException
	 */
	public static VistaLinkConnectionX create(String stationNo, String duz) 
			throws VistaLinkConnectionException, InvalidStationMappingException {
		
		return VistaLinkConnectionCreator.create(stationNo, duz, 
				CONNECTION_ATTEMPTS, WAIT_BETWEEN_RETRIES);
		
	}
	
	
	/**
	 * return a VistaLinkConnectionX containing a vista link connection
	 * 
	 * @param stationNo
	 * @param duz
	 * @param connectionAttempts
	 * @param waitBetweenRetries
	 * @return
	 * @throws VistaLinkConnectionException
	 * @throws InvalidStationMappingException
	 */
	public static VistaLinkConnectionX create(String stationNo, String duz, 
			int connectionAttempts, int waitBetweenRetries) 
			throws VistaLinkConnectionException, InvalidStationMappingException {
		
		return VistaLinkConnectionX.builder()
			.sta3n(stationNo)
			.conn(VistaLinkUtils.getConnectionFromUserInfo(stationNo, null, duz, 
					connectionAttempts, waitBetweenRetries))
			.build();
		
	}
	
	
	
}
