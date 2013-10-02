package ananas.jing.lite.core.util;

import java.util.List;
import java.util.Vector;

public interface STRunner {

	void add(Runnable runn);

	void start();

	class Factory {

		public static STRunner newInstance() {
			return new TheImpl();
		}

		private static class TheImpl implements STRunner, Runnable {

			private final List<Runnable> list = new Vector<Runnable>();
			private int _count_thread;

			@Override
			public void add(Runnable runn) {
				list.add(runn);
				this.start();
			}

			@Override
			public void start() {

				if (this._count_thread <= 0) {
					(new Thread(this)).start();
				}

			}

			@Override
			public synchronized void run() {

				this._count_thread++;
				System.out.println(this + ".count_thread = "
						+ this._count_thread);

				long now, last_time;
				last_time = now = System.currentTimeMillis();

				for (; (now - last_time) < 5000;) {
					Runnable runn = null;
					try {
						if (list.size() > 0) {
							runn = list.remove(0);
							runn.run();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					now = System.currentTimeMillis();
					if (runn != null) {
						last_time = now;
					} else {
						this.__safe_sleep(200);
					}
				}

				this._count_thread--;
				System.out.println(this + ".count_thread = "
						+ this._count_thread);
			}

			private void __safe_sleep(int ms) {
				try {
					Thread.sleep(ms);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
