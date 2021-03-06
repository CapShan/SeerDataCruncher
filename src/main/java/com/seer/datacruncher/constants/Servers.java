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
package com.seer.datacruncher.constants;

public enum Servers {
	FTP(1,"FTP"), JMS(2,"JMS"), WEBSERVICE(3,"WebServices"), SAMBA(4, "SAMBAWIN"), HTTP(5, "HTTP");
	
	int dbCode;
	String name;
	Servers(int dbCode,String name) {
        this.dbCode = dbCode;
        this.name = name;
    }
    public int getDbCode() {
        return dbCode;
    }
    public String getName(){
    	return name;
    }
    public static Servers getStatus(int dbCode) {
    	Servers[] serversList = Servers.values();
        for (Servers server : serversList) {
			if (dbCode == server.getDbCode()) {
                return server;
            }
        }
        return null;
    }
}
