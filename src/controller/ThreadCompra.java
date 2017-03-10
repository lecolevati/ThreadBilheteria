package controller;

import java.util.concurrent.Semaphore;

public class ThreadCompra extends Thread {

	private static int totalIngressos = 100;
	private int id, qtd;
	private Semaphore semaforo;
	private boolean liberado;

	public ThreadCompra(int id, int qtd, Semaphore semaforo) {
		this.id = id;
		this.qtd = qtd;
		this.semaforo = semaforo;
	}

	@Override
	public void run() {
		login();
		if (liberado) {
			tempoCompra();
			if (liberado) {
				try {
					semaforo.acquire();
					compraIngresso();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					semaforo.release();
				}
			}
		}
	}

	private void tempoCompra() {
		int tempo = (int) ((Math.random() * 2001) + 1000);
		try {
			Thread.sleep(tempo);
			if (tempo >= 2500) {
				System.out.println("Compra do #"+id+" TimeOut");
				liberado = false;
			} else {
				System.out.println("Comprador #" + id + " solicitou a compra");
				liberado = true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void login() {
		int tempo = (int) ((Math.random() * 1951) + 50);
		try {
			Thread.sleep(tempo);
			if (tempo >= 1000) {
				System.out.println("Login do #"+id+" TimeOut");
				liberado = false;
			} else {
				System.out.println("Comprador #" + id + " logado no sistema");
				liberado = true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void compraIngresso() {
		if (totalIngressos - qtd >= 0) {
			totalIngressos -= qtd;
			System.out.println("Comprador #" + id + " comprou seus " + qtd
					+ " ingressos");
			System.out.println("Sobraram "+totalIngressos+" ingressos");
		} else {
			System.out.println("Comprador #" + id + " não comprou seus " + qtd
					+ " ingressos por indisponibilidade da quantidade");
		}
	}

}
