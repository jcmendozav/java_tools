package com.ssh.services;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.ssh.batch.model.SshParamsInput;
import com.ssh.batch.model.SshResult;
import com.ssh.contants.SshStatus;


@Service
public class SshService {

	private static final Logger log = LoggerFactory.getLogger(SshService.class);

	public static SshResult run(SshParamsInput item, int timeout) {
		// TODO Auto-generated method stub

		log.debug("###### start: {}",item);
		SshResult result = new SshResult(item);

		String host = item.getHost();
		String user = item.getUser();
		String password = item.getPassword();
		String command1 = item.getCommand1();
		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.setTimeout(timeout);
			log.info("Connected, conf timeout:{}",session.getTimeout());
			session.connect();

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command1);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					log.debug(new String(tmp, 0, i));
					result.setCommandResult(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					log.info("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
					result.setStatus(SshStatus.ERROR);
					result.setDescription(ee.getMessage());
					log.error(ee.getMessage(), ee);
					
				}
			}
			channel.disconnect();
			session.disconnect();
			result.setStatus(SshStatus.SUCCESS);
			log.info("DONE");
		} catch (Exception e) {
			result.setStatus(SshStatus.ERROR);
			result.setDescription(e.getMessage());
			log.error(e.getMessage(), e);
			;
		}
		
		log.debug("###### end  : {}",item);
		log.debug("###### ");
		log.debug("###### ");

		return result;
	}

}
