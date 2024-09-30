package com.thinking.machines.hr.dl.dao;

import java.util.*;
import java.io.*;

import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.EmployeeDAO;
import com.thinking.machines.hr.dl.dto.*;

public class DesignationDAO implements DesignationDAOInterface {
	private static final String FILE_NAME = "Designation.data";

	public void add(DesignationDTOInterface designationDTO) throws DAOException {
		if (designationDTO == null)
			throw new DAOException("Designation is null");
		String title = designationDTO.getTitle();
		if (title == null)
			throw new DAOException("Designation is null");
		title = title.trim();
		if (title.length() == 0)
			throw new DAOException("Length of Designation is 0");

		try {
			File file = new File(FILE_NAME);
			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file, "rw");

			int lastGeneratedCode = 0;
			int recordCount = 0;
			String lastGeneratedCodeString = "";
			String recordCountString = "";

			if (randomAccessFile.length() == 0) {
				lastGeneratedCodeString = "0";
				recordCountString = "0";

				while (lastGeneratedCodeString.length() < 10)
					lastGeneratedCodeString += " ";
				while (recordCountString.length() < 10)
					recordCountString += " ";

				randomAccessFile.writeBytes(lastGeneratedCodeString);
				randomAccessFile.writeBytes("\n");
				randomAccessFile.writeBytes(recordCountString);
				randomAccessFile.writeBytes("\n");
			} else {
				lastGeneratedCodeString = randomAccessFile.readLine().trim();
				recordCountString = randomAccessFile.readLine().trim();

				lastGeneratedCode = Integer.parseInt(lastGeneratedCodeString);
				recordCount = Integer.parseInt(recordCountString);
			}

			int fcode;
			String ftitle;
			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				fcode = Integer.parseInt(randomAccessFile.readLine());
				ftitle = randomAccessFile.readLine();

				if (ftitle.equalsIgnoreCase(title)) {
					randomAccessFile.close();
					throw new DAOException("Designation : " + title + " exists");
				}
			}

			int code;
			code = lastGeneratedCode + 1;
			designationDTO.setCode(code);

			randomAccessFile.writeBytes(String.valueOf(code));
			randomAccessFile.writeBytes("\n");
			randomAccessFile.writeBytes(title);
			randomAccessFile.writeBytes("\n");

			lastGeneratedCode++;
			recordCount++;
			lastGeneratedCodeString = String.valueOf(lastGeneratedCode);
			while (lastGeneratedCodeString.length() < 10)
				lastGeneratedCodeString += " ";
			recordCountString = String.valueOf(recordCount);
			while (recordCountString.length() < 10)
				recordCountString += " ";

			randomAccessFile.seek(0);
			randomAccessFile.writeBytes(lastGeneratedCodeString);
			randomAccessFile.writeBytes("\n");
			randomAccessFile.writeBytes(recordCountString);
			randomAccessFile.writeBytes("\n");

			randomAccessFile.close();
		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}

	}

	public void update(DesignationDTOInterface designationDTO) throws DAOException {
		if (designationDTO == null)
			throw new DAOException("Designation is null");

		int code = designationDTO.getCode();
		if (code <= 0)
			throw new DAOException("Invalid Code : " + code);

		String title = designationDTO.getTitle();
		if (title == null)
			throw new DAOException("Designation is null");
		title = title.trim();
		if (title.length() == 0)
			throw new DAOException("Length of title is 0");

		try {
			File file = new File(FILE_NAME);
			if (!file.exists())
				throw new DAOException("Invalid Code : " + code);

			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file, "rw");
			if (randomAccessFile.length() == 0) {
				randomAccessFile.close();
				throw new DAOException("Invalid Code : " + code);
			}

			randomAccessFile.readLine();
			randomAccessFile.readLine();

			String ftitle;
			int fcode;
			boolean found = false;

			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				fcode = Integer.parseInt(randomAccessFile.readLine());

				if (fcode == code) {
					found = true;
					break;
				}
				randomAccessFile.readLine();
			}

			if (found == false) {
				randomAccessFile.close();
				throw new DAOException("Invalid Code : " + code);
			}

			randomAccessFile.seek(0);
			randomAccessFile.readLine();
			randomAccessFile.readLine();

			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				fcode = Integer.parseInt(randomAccessFile.readLine());
				ftitle = randomAccessFile.readLine();

				if (fcode != code && ftitle.equalsIgnoreCase(title) == true) {
					randomAccessFile.close();
					throw new DAOException("Title : " + title + " exists");
				}
			}

			File tfile = new File("temp.data");
			if (tfile.exists())
				tfile.delete();

			RandomAccessFile tempRandomAccessFile;
			tempRandomAccessFile = new RandomAccessFile(tfile, "rw");

			randomAccessFile.seek(0);

			tempRandomAccessFile.writeBytes(randomAccessFile.readLine());
			tempRandomAccessFile.writeBytes("\n");
			tempRandomAccessFile.writeBytes(randomAccessFile.readLine());
			tempRandomAccessFile.writeBytes("\n");

			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				fcode = Integer.parseInt(randomAccessFile.readLine());
				ftitle = randomAccessFile.readLine();

				if (fcode != code) {
					tempRandomAccessFile.writeBytes(String.valueOf(fcode));
					tempRandomAccessFile.writeBytes("\n");
					tempRandomAccessFile.writeBytes(ftitle);
					tempRandomAccessFile.writeBytes("\n");
				} else {
					tempRandomAccessFile.writeBytes(String.valueOf(code));
					tempRandomAccessFile.writeBytes("\n");
					tempRandomAccessFile.writeBytes(title);
					tempRandomAccessFile.writeBytes("\n");
				}

			}

			randomAccessFile.seek(0);
			tempRandomAccessFile.seek(0);

			while (tempRandomAccessFile.getFilePointer() < tempRandomAccessFile.length()) {
				randomAccessFile.writeBytes(tempRandomAccessFile.readLine());
				randomAccessFile.writeBytes("\n");
			}

			randomAccessFile.setLength(tempRandomAccessFile.length());
			tempRandomAccessFile.setLength(0);

			randomAccessFile.close();
			tempRandomAccessFile.close();
		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}

	}

	public void delete(int code) throws DAOException {
		if (code <= 0)
			throw new DAOException("Invalid Code : " + code);

		try 
		{
			String fTitle = "";
			File file = new File(FILE_NAME);
			if (!file.exists())
				throw new DAOException("Invalid Code : " + code);

			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file, "rw");
			if (randomAccessFile.length() == 0) {
				randomAccessFile.close();
				throw new DAOException("Invalid Code : " + code);
			}

			randomAccessFile.readLine();
			int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());

			String ftitle;
			int fcode;
			boolean found = false;

			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) 
			{
				fcode = Integer.parseInt(randomAccessFile.readLine());
				fTitle = randomAccessFile.readLine();

				if (fcode == code) 
				{
					found = true;
					break;
				}
			}

			if (found == false) 
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Code : " + code);
			}

			if (new EmployeeDAO().isDesignationAlloted(code))
			{
				randomAccessFile.close();
				throw new DAOException("Employee Exists with Designation : " + fTitle);
			}

			File tfile = new File("temp.data");
			if (tfile.exists())
				tfile.delete();

			RandomAccessFile tempRandomAccessFile;
			tempRandomAccessFile = new RandomAccessFile(tfile, "rw");

			randomAccessFile.seek(0);

			tempRandomAccessFile.writeBytes(randomAccessFile.readLine());
			tempRandomAccessFile.writeBytes("\n");
			tempRandomAccessFile.writeBytes(randomAccessFile.readLine());
			tempRandomAccessFile.writeBytes("\n");

			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) 
			{
				fcode = Integer.parseInt(randomAccessFile.readLine());
				ftitle = randomAccessFile.readLine();

				if (fcode != code) {
					tempRandomAccessFile.writeBytes(String.valueOf(fcode));
					tempRandomAccessFile.writeBytes("\n");
					tempRandomAccessFile.writeBytes(ftitle);
					tempRandomAccessFile.writeBytes("\n");
				}
			}

			randomAccessFile.seek(0);
			tempRandomAccessFile.seek(0);
			randomAccessFile.writeBytes(tempRandomAccessFile.readLine());
			randomAccessFile.writeBytes("\n");
			String recordCountString = String.valueOf(recordCount - 1);
			while (recordCountString.length() < 10)
				recordCountString += " ";
			randomAccessFile.writeBytes(recordCountString);
			randomAccessFile.writeBytes("\n");
			tempRandomAccessFile.readLine();

			while (tempRandomAccessFile.getFilePointer() < tempRandomAccessFile.length()) {
				randomAccessFile.writeBytes(tempRandomAccessFile.readLine());
				randomAccessFile.writeBytes("\n");
			}

			randomAccessFile.setLength(tempRandomAccessFile.length());
			tempRandomAccessFile.setLength(0);

			randomAccessFile.close();
			tempRandomAccessFile.close();
		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}

	}

	public Set<DesignationDTOInterface> getAll() throws DAOException {
		Set<DesignationDTOInterface> designations;
		designations = new TreeSet<>();

		try {
			File file = new File(FILE_NAME);
			if (!file.exists())
				return designations;
			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file, "rw");
			if (randomAccessFile.length() == 0) {
				randomAccessFile.close();
				return designations;
			}

			randomAccessFile.readLine();
			randomAccessFile.readLine();

			DesignationDTOInterface designationDTO;

			int code;
			String title;
			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				code = Integer.parseInt(randomAccessFile.readLine());
				title = randomAccessFile.readLine();
				designationDTO = new DesignationDTO();
				designationDTO.setCode(code);
				designationDTO.setTitle(title);

				designations.add(designationDTO);
			}

			randomAccessFile.close();
			return designations;
		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}
	}

	public DesignationDTOInterface getByCode(int code) throws DAOException {
		if (code <= 0)
			throw new DAOException("Invalid Code : " + code);

		try {
			File file = new File(FILE_NAME);
			if (file.exists() == false)
				throw new DAOException("Invalid Code : " + code);

			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			if (randomAccessFile.length() == 0) {
				randomAccessFile.close();
				throw new DAOException("Invalid Code : " + code);
			}

			randomAccessFile.readLine();
			int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
			if (recordCount == 0) {
				randomAccessFile.close();
				throw new DAOException("Invalid Code : " + code);
			}

			int fCode;
			String ftitle;
			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				fCode = Integer.parseInt(randomAccessFile.readLine());
				ftitle = randomAccessFile.readLine();

				if (fCode == code) {
					randomAccessFile.close();
					DesignationDTOInterface designationDTO;
					designationDTO = new DesignationDTO();
					designationDTO.setCode(code);
					designationDTO.setTitle(ftitle);
					return designationDTO;
				}
			}
			randomAccessFile.close();
			throw new DAOException("Invalid Code : " + code);

		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}
	}

	public DesignationDTOInterface getByTitle(String title) throws DAOException {
		if (title == null || title.trim().length() == 0)
			throw new DAOException("Invalid Title : " + title);
		title = title.trim();

		try {
			File file = new File(FILE_NAME);
			if (file.exists() == false)
				throw new DAOException("Invalid Title : " + title);

			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file, "rw");

			if (randomAccessFile.length() == 0)
				throw new DAOException("Invalid Title : " + title);

			randomAccessFile.readLine();
			randomAccessFile.readLine();

			int fCode;
			String fTitle;
			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				fCode = Integer.parseInt(randomAccessFile.readLine());
				fTitle = randomAccessFile.readLine();

				if (fTitle.equalsIgnoreCase(title)) {
					randomAccessFile.close();
					DesignationDTOInterface designationDTO;
					designationDTO = new DesignationDTO();
					designationDTO.setCode(fCode);
					designationDTO.setTitle(fTitle);
					return designationDTO;
				}
			}

			randomAccessFile.close();
			throw new DAOException("Invalid Title :" + title);
		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}
	}

	public boolean codeExists(int code) throws DAOException {
		if (code <= 0)
			return false;

		try {
			File file = new File(FILE_NAME);
			if (file.exists() == false)
				return false;

			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			if (randomAccessFile.length() == 0) {
				randomAccessFile.close();
				return false;
			}

			randomAccessFile.readLine();
			int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
			if (recordCount == 0) {
				randomAccessFile.close();
				return false;
			}

			int fCode;
			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				fCode = Integer.parseInt(randomAccessFile.readLine());

				if (fCode == code) {
					randomAccessFile.close();
					return true;
				}
				randomAccessFile.readLine();

			}
			randomAccessFile.close();
			return false;

		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}

	}

	public boolean titleExists(String title) throws DAOException {
		if (title == null || title.trim().length() == 0)
			return false;
		title = title.trim();

		try {
			File file = new File(FILE_NAME);
			if (file.exists() == false)
				return false;

			RandomAccessFile randomAccessFile;
			randomAccessFile = new RandomAccessFile(file, "rw");

			if (randomAccessFile.length() == 0)
				return false;

			randomAccessFile.readLine();
			randomAccessFile.readLine();

			String fTitle;
			while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
				Integer.parseInt(randomAccessFile.readLine());
				fTitle = randomAccessFile.readLine();

				if (fTitle.equalsIgnoreCase(title)) {
					randomAccessFile.close();
					return true;
				}
			}

			randomAccessFile.close();
			return false;
		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}
	}

	// **************************************************

	// GET COUNT code
	public int getCount() throws DAOException {
		try {
			File file = new File(FILE_NAME);
			if (file.exists() == false)
				return 0;

			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

			if (randomAccessFile.length() == 0) {
				randomAccessFile.close();
				return 0;
			}

			randomAccessFile.readLine();
			int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());

			randomAccessFile.close();
			return recordCount;
		} catch (IOException io) {
			throw new DAOException(io.getMessage());
		}
	}
}
