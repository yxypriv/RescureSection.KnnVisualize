package utils.general;

public class Triple<P, K, L> {
	P v1;
	K v2;
	L v3;
	
	public Triple(P v1, K v2, L v3) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public P getV1() {
		return v1;
	}

	public void setV1(P v1) {
		this.v1 = v1;
	}

	public K getV2() {
		return v2;
	}

	public void setV2(K v2) {
		this.v2 = v2;
	}

	public L getV3() {
		return v3;
	}

	public void setV3(L v3) {
		this.v3 = v3;
	}

	@Override
	public int hashCode() {
		return v1.hashCode() + v2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!Triple.class.isInstance(obj))
			return false;
		try {
			@SuppressWarnings("unchecked")
			Triple<P, K, L> pObj = (Triple<P, K, L>) obj;
			return this.v1.equals(pObj.getV1()) //
					&& this.v2.equals(pObj.getV2())//
					&& this.v3.equals(pObj.getV3());
		} catch (ClassCastException e) {
			return false;
		}
	}
}
