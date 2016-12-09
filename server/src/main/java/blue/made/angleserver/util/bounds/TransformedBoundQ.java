package blue.made.angleserver.util.bounds;

/**
 * Created by Sam Sartor on 6/3/2016.
 */
public abstract class TransformedBoundQ implements BoundQ {
	private int xx = 1;
	private int xy = 0;
	private int yx = 0;
	private int yy = 1;
	private int gx = 0;
	private int gy = 0;

	private int Tx(int x, int y) {
		return x * xx + y * yx + gx;
	}

	private int Ty(int x, int y) {
		return x * xy + y * yy + gy;
	}

	public void reset() {
		xx = 1;
		xy = 0;
		yx = 0;
		yy = 1;
		gx = 0;
		gy = 0;
	}

	@Override
	public void translate(int byX, int byY) {
		gx += byX * xx + byY * yx;
		gy += byX * xy + byY * yy;
	}

	@Override
	public void rotate(int quarters) {
		int xxn;
		int xyn;
		int yxn;
		int yyn;
		switch (((quarters % 4) + 4) % 4) {
			case 1:
				xxn = yx;
				yxn = -xx;
				xyn = yy;
				yyn = -xy;
				break;
			case 2:
				xx *= -1;
				xy *= -1;
				yx *= -1;
				yy *= -1;
				return;
			case 3:
				xxn = -yx;
				yxn = xx;
				xyn = -yy;
				yyn = xy;
				break;
			default:
				return;
		}
		xx = xxn;
		xy = xyn;
		yx = yxn;
		yy = yyn;
	}

	@Override
	public void flipX() {
		xx *= -1;
		xy *= -1;
	}

	@Override
	public void flipY() {
		yx *= -1;
		yy *= -1;
	}

	@Override
	public boolean box(int fromx, int fromy, int tox, int toy) {
		int x0 = Tx(fromx, fromy);
		int y0 = Ty(fromx, fromy);
		int x1 = Tx(tox, toy);
		int y1 = Ty(tox, toy);
		if (x1 < x0) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y1 < y0) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		return doBox(x0, y0, x1, y1);
	}

	public abstract boolean doBox(int fromx, int fromy, int tox, int toy);

	@Override
	public boolean single(int x, int y) {
		return doSingle(Tx(x, y), Ty(x, y));
	}

	public abstract boolean doSingle(int x, int y);
}
