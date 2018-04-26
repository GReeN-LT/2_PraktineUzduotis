package com.DiagnozeIrGydimas;

public enum Rodikliai
	{

	WBC("Leukocitų kiekis", "4", "10"),
	NEUT("Neutrofilai", "40", "80"),
	EOS	("Eozinofilai", "1", "6"),
	BASO("Bazofilai", "0", "2"),
	LUC("LUC", "0", "4"),
	GRA("Granuliocitų dalis", "40", "70"),
	PLT("Trombocitai", "180", "320"),
	MCV("Vidutinis eritrocito tūris", "82", "98"),
	MCH("Vidutinis eritrocitų hemoglobinas", "26", "31"),
	NE("Neutrofilai", "1.5", "4.5");

	final String desc;
	final String Min;
	final String Max;

	Rodikliai (String aprasymas, String Minim, String Maxi)
	{
		desc = aprasymas;
		Min = Minim;
		Max = Maxi;
	}

	public String getDesc()
	{
		return desc;
	}

	public String getMin()
	{
		return Min;
	}

	public String getMax()
	{
		return Max;
	}



	}
