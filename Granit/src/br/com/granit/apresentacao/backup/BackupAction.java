package br.com.granit.apresentacao.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import sun.swing.BakedArrayList;

import br.com.granit.apresentacao.PrincipalAction;
import br.com.granit.util.Constantes;
import br.com.granit.util.Formatador;


public class BackupAction extends PrincipalAction {

	public ActionForward criarBackup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String filePath = "C://backupGranit//";
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			String data = Formatador.converterDataString(new Date(),
					Formatador.FORMATO_DATA_HORA_BACKUP);
			
			String nomeDatatabeFile = "backup" + data + ".sql";			
			//File databaseBackupFile = new File(filePath + File.separator + nomeDatatabeFile);
			//String cmd = "cmd.exe /c mysqldump -ugranit -pgr@n!t granitdb > "
			//		+ databaseBackupFile.getAbsolutePath();
			
			
			String cmd = "mysqldump";
			ProcessBuilder pb = new ProcessBuilder(cmd, "--user=granit", "--password=gr@n!t", "granitdb", "--result-file=" + (filePath+nomeDatatabeFile));
            pb.start();
			
			

//			if (!databaseBackupFile.isFile()) {
//				Process processo = Runtime.getRuntime().exec(cmd);
//				BufferedReader in = new BufferedReader(
//                        new InputStreamReader(processo.getInputStream()));
//			    String line = null;
//			    while ((line = in.readLine()) != null) {
//			        System.out.println(line);
//			    }
//			} else {
//				int numerodobackup = 1;
//				while (databaseBackupFile.isFile()) {
//					numerodobackup++;
//					databaseBackupFile = new File(filePath
//							+ File.separator
//							+ nomeDatatabeFile.replace(".sql", "_" + numerodobackup
//									+ ".sql"));
//				}
//				cmd = "cmd /c \"mysqldump -ugranit -pgr@n!t granitdb > "
//						+ databaseBackupFile.getAbsolutePath() + "\"";
//			}
			adicionaMensagem(request, Constantes.MSG_OPERACAO_SUCESSO);
		} catch (IOException e) {
			e.printStackTrace();
			adicionaErro(request, "backup.nao.realizado");
		}
		return listarBackups(mapping, form, request, response);
	}

	public ActionForward listarBackups(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		cleanForm(form);
		String filePath = "C://backupGranit";
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		int i = 0;
		List<BackupTO> backups = new ArrayList<BackupTO>();
		for (File backupFile : file.listFiles()) {
			BackupTO backupTO = new BackupTO();
			backupTO.setIdBackup(i++);
			backupTO.setPath(backupFile.getAbsolutePath());
			backupTO.setNome(backupFile.getName());
			String data = backupFile.getName().replace("backup", "");
			Date date;
			try {
				date = Formatador.converterStringParaDate(data,
						Formatador.FORMATO_DATA_HORA_BACKUP);
				backupTO.setDate(date);
				backupTO.setData(Formatador.converterDataString(date,
						Formatador.FORMATO_DATA_HORA));
			} catch (ParseException e) {
				backupTO.setData(data);
			}
			backups.add(backupTO);
		}
		Collections.sort(backups);
		request.setAttribute("listaBackups", backups);
		request.getSession().setAttribute("listaBackups", backups);
		return (mapping.findForward("listar"));
	}

	public ActionForward downloadBackup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		cleanForm(form);
		List<BackupTO> backups = (List<BackupTO>) request.getSession()
				.getAttribute("listaBackups");
		String idBackup = request.getParameter("idBackup");
		if (idBackup == null || idBackup.isEmpty()) {
			idBackup = request.getAttribute("idBackup").toString();
		}
		if (idBackup == null) {
			request.setAttribute("listaBackups", backups);
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			return (mapping.findForward("listar"));
		}
		BackupTO backup = backups.get(0);
		
		for (BackupTO backupTO : backups) {
			if(backupTO.getIdBackup().intValue() == Integer.valueOf(idBackup).intValue()){
				backup = backupTO;
				break;
			}
		}
		
		File file = new File(backup.getPath());
		System.out.println("Downloading file " + file);

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ file.getName());
		try {
			// Get it from file system
			FileInputStream in = new FileInputStream(file);
			ServletOutputStream out = response.getOutputStream();
			byte[] outputByte = new byte[4096];
			// copy binary content to output stream
			while (in.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
			}
			in.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward processarExcluir(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String idBackup = request.getParameter("idBackup");
		List<BackupTO> listaDeBackups = (List<BackupTO>) request.getSession().getAttribute("listaBackups");
		boolean removeu = false;
		for (BackupTO backupTO : listaDeBackups) {
			if (backupTO.getIdBackup().toString().equals(idBackup)){
				removeu = new File(backupTO.getPath()).delete();
				break;
			}
		}
		if (removeu){
			adicionaMensagem(request, Constantes.EXCLUSAO_SUCESSO);
		}else{
			adicionaErro(request, Constantes.MSG_ERRO_REMOCAO);
		}
		return listarBackups(mapping, form, request, response);
	}

	@Override
	protected String getTableId() {
		// TODO Auto-generated method stub
		return null;
	}

}
