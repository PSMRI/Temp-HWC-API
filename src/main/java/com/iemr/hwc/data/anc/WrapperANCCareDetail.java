/*
* AMRIT – Accessible Medical Records via Integrated Technology 
* Integrated EHR (Electronic Health Records) Solution 
*
* Copyright (C) "Piramal Swasthya Management and Research Institute" 
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.iemr.hwc.data.anc;

import java.util.List;

public class WrapperANCCareDetail {
	
	private ANCCareDetails ancCareDetails;
	
	private List<ANCWomenVaccineDetail> ancWomenVaccineDetail;

	public ANCCareDetails getAncCareDetails() {
		return ancCareDetails;
	}

	public void setAncCareDetails(ANCCareDetails ancCareDetails) {
		this.ancCareDetails = ancCareDetails;
	}

	public List<ANCWomenVaccineDetail> getAncWomenVaccineDetail() {
		return ancWomenVaccineDetail;
	}

	public void setAncWomenVaccineDetail(List<ANCWomenVaccineDetail> ancWomenVaccineDetail) {
		this.ancWomenVaccineDetail = ancWomenVaccineDetail;
	}

}
