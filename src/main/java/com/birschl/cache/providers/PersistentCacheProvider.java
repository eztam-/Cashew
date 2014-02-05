/* Copyright (C) 2014 by Matthias Birschl (m-birschl@gmx.de)
 * 
 * This file is part of Cashew.
 * Cashew is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * Cashew is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.birschl.cache.providers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//TODO more than fife params exception,
// TODO also support Object cacheScope? If not throw an exception if object scope is selected
// TODO close the connection
public class PersistentCacheProvider extends CacheProvider {

	private String methodTableName;
	private static Connection connection;

	public PersistentCacheProvider(String uniqueMethodId) throws Exception { // TODO exceptionhandling
		super(uniqueMethodId);
		methodTableName = "cache_" + uniqueMethodId.hashCode();
		methodTableName = methodTableName.replace("-", "m");

		if (connection == null)
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			Properties properties = new Properties();
			properties.put("user", "userName");
			properties.put("password", "userPassword");
			connection = DriverManager.getConnection("jdbc:derby:CASHEW;create=true;", properties);
		}
		createTableIfnotExist(connection);
	}

	private void createTableIfnotExist(Connection connection) throws Exception {
		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			statement
					.executeUpdate("CREATE TABLE " + methodTableName + " (ARG1 INT, ARG2 INT, ARG3 INT, ARG4 INT, ARG5 INT, VALUE BLOB(1m))"); // TODO make the blob size configurable with a static field

		} catch (SQLException e)
		{
			if (!e.getSQLState().equals("X0Y32"))
			{
				throw e;
			}
			// Do nothing if the table already exist 
		} finally
		{
			if (statement != null)
				statement.close();
		}

	}

	@Override
	public Object getCachedResult(Object[] keys) {
		Object result = null;
		try
		{
			ResultSet rs = selectValue(keys);
			if (rs.next())
			{
				Blob blob = rs.getBlob("VALUE");
				ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
				result = ois.readObject();
				ois.close();
				blob.free();
			}
			rs.getStatement().close();
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public void put(Object[] keys, Object value) {
		try
		{
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO " + methodTableName + " ( ARG1, ARG2, ARG3, ARG4, ARG5, VALUE) " + "VALUES(?,?,?,?,?,?)");
			for (int i = 0; i < 5; i++)
			{
				if (keys.length > i)
					ps.setInt(i + 1, keys[i].hashCode());
				else
					ps.setInt(i + 1, 0);
			}
			Blob blob = connection.createBlob();
			ObjectOutputStream oos; // TODO use XML marshalling instead of ObjectOutputStream  because of incompatibility issues between different JREs
			oos = new ObjectOutputStream(blob.setBinaryStream(1));
			oos.writeObject(value);
			oos.close();
			ps.setBlob(6, blob);
			ps.execute();
			blob.free();
			ps.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	@Override
	public boolean contains(Object[] keys) {
		boolean exists = false;
		try
		{
			ResultSet rs = selectValue(keys);
			exists = rs.next();
			rs.getStatement().close();
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		return exists;
	}

	@Override
	public void setExpirationTime(long expirationTime) {
		// TODO Auto-generated method stub

	}

	private ResultSet selectValue(Object[] keys) {
		ResultSet rs = null;
		try
		{
			PreparedStatement ps = connection
					.prepareStatement("SELECT VALUE FROM " + methodTableName + " WHERE ARG1 = ? AND ARG2 = ? AND ARG3 = ? AND ARG4 = ? AND ARG5 = ?");
			for (int i = 0; i < 5; i++)
			{
				if (keys.length > i)
					ps.setInt(i + 1, keys[i].hashCode());
				else
					ps.setInt(i + 1, 0);
			}
			rs = ps.executeQuery();

		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		return rs;
	}
}
