/*
 * Copyright (c) 2015  www.see-r.com
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.seer.datacruncher.spring;

import com.seer.datacruncher.constants.Mail;
import com.seer.datacruncher.jpa.Validate;
import com.seer.datacruncher.jpa.dao.DaoSet;
import com.seer.datacruncher.jpa.entity.ApplicationConfigEntity;
import com.seer.datacruncher.utils.CryptoUtil;
import com.seer.datacruncher.utils.generic.CommonUtils;
import com.seer.datacruncher.utils.generic.I18n;
import com.seer.datacruncher.utils.mail.MailConfig;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
 
public class ApplicationConfigCheckController implements Controller, DaoSet {

	private String mailFrom;
	private String mailTo;
	private String mailSubject;
	private String mailTemplate;
	private VelocityEngine velocityEngine;
	
	public ModelAndView handleRequest(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		
		Validate validate = new Validate();
				
		ServletOutputStream out = null;
		response.setContentType("application/json");
		out = response.getOutputStream();	
		
		String configType = request.getParameter("configType");
		
		if(configType.equals("email")) {
			ApplicationConfigEntity appConfigEntity = new ApplicationConfigEntity();
			appConfigEntity.setUserName(request.getParameter("userName"));
			appConfigEntity.setPassword(new CryptoUtil().encrypt(request.getParameter("password")));			
			appConfigEntity.setHost(request.getParameter("host"));
			appConfigEntity.setPort(request.getParameter("port"));
			appConfigEntity.setProtocol(request.getParameter("protocol"));
			appConfigEntity.setEncoding(request.getParameter("encoding"));
			appConfigEntity.setSmtpsTimeout(request.getParameter("smtpstimeout"));
			appConfigEntity.setIsStarTtls(Integer.parseInt(request.getParameter("starttls") == null ? "0" : request.getParameter("starttls")));
			appConfigEntity.setIsSmtpsAuthenticate(Integer.parseInt(request.getParameter("smtpsAuthenticate") == null ? "0" : request.getParameter("smtpsAuthenticate")));
			
			Mail.instiateMailService(appConfigEntity);
			
			MailConfig mailConfig = new MailConfig();
			mailConfig.setMailTo(mailTo);
			mailConfig.setMailFrom(mailFrom);
			mailConfig.setSubject(mailSubject);
			String mailContent = CommonUtils.mergeVelocityTemplateForEmail(velocityEngine, mailTemplate, null);
			mailConfig.setText(mailContent);
			try {
				Mail.getJavaMailService().sendMail(mailConfig);
				validate.setSuccess(true);
				validate.setMessage(I18n.getMessage("success.validEmailCredential"));
			} catch(Exception ex) {
				validate.setSuccess(false);
				validate.setMessage(I18n.getMessage("error.invalidEmailCredential"));				
			} finally {
				Mail.instiateMailService(null);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		out.write(mapper.writeValueAsBytes(validate));
		out.flush();
		out.close();
 		
 		return null;
	}

	//----------------SETTERS & GETTERS-------------
	
	/**
	 * @return the mailFrom
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * @param mailFrom the mailFrom to set
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
		
	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return mailTo;
	}

	/**
	 * @param mailTo the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	/**
	 * @return the mailSubject
	 */
	public String getMailSubject() {
		return mailSubject;
	}

	/**
	 * @param mailSubject the mailSubject to set
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(String mailTemplate) {
		this.mailTemplate = mailTemplate;
	}
	
	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}