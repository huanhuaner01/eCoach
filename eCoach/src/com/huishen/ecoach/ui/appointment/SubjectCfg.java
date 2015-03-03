package com.huishen.ecoach.ui.appointment;

class SubjectCfg {
	protected String name;
	protected int amCount;
	protected int pmCount;
	protected int ntCount;
	protected String amtime;
	protected String pmtime;
	protected String nttime;

	protected SubjectCfg() {

	}

	protected SubjectCfg(String name, int amCount, int pmCount, int nightCount,
			String amtime, String pmtime, String nttime) {
		super();
		this.name = name;
		this.amCount = amCount;
		this.pmCount = pmCount;
		this.ntCount = nightCount;
		this.amtime = amtime;
		this.pmtime = pmtime;
		this.nttime = nttime;
	}

	protected int getAppointLimit() {
		return amCount + pmCount + ntCount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAmCount(int amCount) {
		this.amCount = amCount;
	}

	public void setPmCount(int pmCount) {
		this.pmCount = pmCount;
	}

	public void setNtCount(int ntCount) {
		this.ntCount = ntCount;
	}

	public void setAmtime(String amtime) {
		this.amtime = amtime;
	}

	public void setPmtime(String pmtime) {
		this.pmtime = pmtime;
	}

	public void setNttime(String nttime) {
		this.nttime = nttime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubjectCfg [name=").append(name).append(", amCount=")
				.append(amCount).append(", pmCount=").append(pmCount)
				.append(", ntCount=").append(ntCount).append(", amtime=")
				.append(amtime).append(", pmtime=").append(pmtime)
				.append(", nttime=").append(nttime).append("]");
		return builder.toString();
	}

}