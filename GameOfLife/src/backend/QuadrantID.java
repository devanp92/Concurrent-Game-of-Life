package backend;

public enum QuadrantID {
	TOP_LEFT, TOP_RIGHT, BOT_LEFT, BOT_RIGHT;

	public int getHalfWidth(int width) {
		int retVal = width/2;
		if(this == TOP_RIGHT || this == BOT_RIGHT) {
			retVal = width-width/2;
		}
		return retVal;
	}

	public int getHalfHeight(int height) {
		int retVal = height/2;
		if(this == BOT_LEFT || this == BOT_RIGHT) {
			retVal = height-height/2;
		}
		return retVal;
	}

	public int getXOffset(int width) {
		int retVal = 0;
		if(this == TOP_RIGHT || this == BOT_RIGHT) {
			retVal = width/2;
		}
		return retVal;
	}

	public int getYOffset(int height) {
		int retVal = 0;
		if(this == BOT_LEFT || this == BOT_RIGHT) {
			retVal = height/2;
		}
		return retVal;
	}
}
