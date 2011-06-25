/**
* H2 driver for Owners table.
* Copyright (C) 2011 BigBrother Contributors
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package me.taylorkelly.bigbrother.tablemgrs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.taylorkelly.bigbrother.BBLogging;
import me.taylorkelly.bigbrother.BBPlayerInfo;
import me.taylorkelly.bigbrother.datasource.BBDB;

/**
 * @author Rob
 *
 */
public class OwnersH2 extends OwnersTable {

    /* (non-Javadoc)
     * @see me.taylorkelly.bigbrother.tablemgrs.OwnersTable#setBlockOwner(int, int, int, int, int)
     */
    @Override
    protected void setBlockOwner(int world, int x, int y, int z, int playerID) {
        if(!trySetBlockOwnerUpdate(world, x,y,z,playerID)) {
            setBlockOwnerInsert(world,x,y,z,playerID);
        }
    }
    protected boolean trySetBlockOwnerUpdate(int world,int x,int y, int z, int playerID) {
        PreparedStatement stmt=null;
        try {
            stmt = BBDB.prepare("UPDATE "+getTableName()+" SET plyID=? WHERE wldID=? AND x=? AND y=? AND z=?");
            stmt.setInt(0, playerID);
            stmt.setInt(1, world);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);
            return stmt.execute();
        } catch (SQLException e) {
            BBLogging.severe("Error when performing setBlockOwner in OwnersH2: ",e);
        } finally {
            BBDB.cleanup("OwnersH2.setBlockOwner", stmt, null);
        }
        return false;
    }
    protected void setBlockOwnerInsert(int world,int x,int y, int z, int playerID) {
        PreparedStatement stmt=null;
        try {
            stmt = BBDB.prepare("INSERT INTO "+getTableName()+" (wldID,x,y,z,usrID) VALUES (?,?,?,?,?)");
            stmt.setInt(0, world);
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, z);
            stmt.setInt(4, playerID);
            stmt.execute();
        } catch (SQLException e) {
            BBLogging.severe("Error when performing setBlockOwner in OwnersH2: ",e);
        } finally {
            BBDB.cleanup("OwnersH2.setBlockOwner", stmt, null);
        }
    }
    
    /* (non-Javadoc)
     * @see me.taylorkelly.bigbrother.tablemgrs.OwnersTable#getBlockOwner(int, int, int, int)
     */
    @Override
    protected int getBlockOwner(int world, int x, int y, int z) {
        PreparedStatement stmt=null;
        ResultSet rs = null;
        try {
            stmt = BBDB.prepare("SELECT FROM "+getTableName()+" WHERE wldID=? AND x=? AND y=? AND z=?");
            stmt.setInt(0, world);
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, z);
            rs = stmt.executeQuery();
            if(rs.next())
                return rs.getInt("usrID");
        } catch (SQLException e) {
            BBLogging.severe("Error when performing setBlockOwner in OwnersH2: ",e);
        } finally {
            BBDB.cleanup("OwnersH2.setBlockOwner", stmt, rs);
        }
        return BBPlayerInfo.ENVIRONMENT.getID();
    }
    
    /* (non-Javadoc)
     * @see me.taylorkelly.bigbrother.tablemgrs.DBTable#onLoad()
     */
    @Override
    protected void onLoad() {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see me.taylorkelly.bigbrother.tablemgrs.DBTable#getCreateSyntax()
     */
    @Override
    public String getCreateSyntax() {
        return "CREATE TABLE IF NOT EXISTS "+getTableName()+" ("
        +"wldID INTEGER,"
        +"x INTEGER,"
        +"y INTEGER,"
        +"z INTEGER,"
        +"usrID INTEGER,"
        +"PRIMARY KEY (wldID,x,y,z)"
        +")";
    }

}
