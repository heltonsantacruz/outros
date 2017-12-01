package br.com.granit.util;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimerListener implements ServletContextListener {

		/**
		* @see ServletContextListener#contextInitialized(ServletContextEvent)
		*/
		public void contextInitialized(ServletContextEvent arg0) {
		
			ServletContext servletContext = arg0.getServletContext();
			
			int delay = 10000;
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){
				public void run(){
					backupBD();
				}//End of Run
			},delay, 86400000); //24horas
			//},delay, 60000);
			servletContext.setAttribute ("timer", timer);
		}

		/**
		* @see ServletContextListener#contextDestroyed(ServletContextEvent)
		*/
		public void contextDestroyed(ServletContextEvent arg0) {
			ServletContext servletContext = arg0.getServletContext();
			// get our timer from the Context
			Timer timer = (Timer)servletContext.getAttribute ("timer");
	
			// cancel all pending tasks in the timers queue
			if (timer != null)
			timer.cancel();
	
			// remove the timer from the servlet context
			servletContext.removeAttribute ("timer");
			System.out.println("ServletContextListener destroyed");
		}



	private void backupBD(){ 
		try {
			String filePath = "C://backupGranit//";
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			String data = Formatador.converterDataString(new Date(),
					Formatador.FORMATO_DATA_HORA_BACKUP);
			
			String nomeDatatabeFile = "backup" + data + ".sql";			
			String cmd = "mysqldump";
			ProcessBuilder pb = new ProcessBuilder(cmd, "--user=granit", "--password=gr@n!t", "granitdb", "--result-file=" + (filePath+nomeDatatabeFile));
	        pb.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro no backup:"+ e.getMessage());
		}
	}	

}
