package com.dream.thread;

import com.dream.view.MainFrame;

public class UpdateStockImageThread implements Runnable {
	private MainFrame mainFrame;
	public UpdateStockImageThread(MainFrame mainFrame){
		this.mainFrame = mainFrame;
	}
	public void run() {
		if(this.mainFrame == null)
			return;
		while(true){
			mainFrame.updateImageForStock();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
