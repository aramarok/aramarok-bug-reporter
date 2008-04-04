package com.drey.aramarok.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.EnumSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserStatus;

public class DataImporter {
	
	public static EntityManagerFactory emf;
	
	private EntityManager em;
	
	private Query userNameQuery;
	
	private Query rolesQuery;
	
	private Query bugQuery;

	public DataImporter() {
		emf = Persistence.createEntityManagerFactory("Aramarok");
		em = emf.createEntityManager();
		
		userNameQuery = em.createQuery("Select u from User u where u.userName =:userName");
		
		rolesQuery = em.createQuery("Select r from Role r where r.name =:roleName");
		
		bugQuery = em.createQuery("Select b from Bug b");
	}
	
	
	public static void main(String[] args) {
		try {
		new DataImporter().importData();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void importData() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
//			importUsers("users.csv");
//			importRoles("roles.csv");
//			importUserRoles("userRoles.csv");
//
//			importOperatingSystem("OS.csv");
//			importPlatform("platform.csv");
//			importPriority("priority.csv");
//			importSeverity("severity.csv");
			
			
//			importBugs("bugs.csv");
			tx.commit();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
	}
	
	private User findUserByUserName(String userName) {
		try {
			return (User) userNameQuery.setParameter("userName", userName).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private Role findRole(String roleName) {
		try {
			return (Role) rolesQuery.setParameter("roleName", roleName).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Imports users (lastName, firstName, middleName, email, userName, password)
	 * @param filename
	 * @throws Exception
	 */
	public void importUsers(String filename) throws Exception {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		
		while(true) {
			String line = br.readLine();
			if (line == null)
				break;
			String[] tokens = line.split(",");
			String userLastName = tokens[0].trim();
			String userFirstName = tokens[1].trim();
			String userMiddleName = tokens[2].trim();			
			String email = tokens[3].trim();
			String userName = tokens[4].trim();
			String password = tokens[5].trim();
			
			System.out.println("Line:"+line);
			System.out.println("LastName:"+userLastName);
			System.out.println("FirstName:"+userFirstName);
			System.out.println("MiddleName:"+userMiddleName);
			System.out.println("Email:"+email);
			System.out.println("UserName:"+userName);
			System.out.println("Password:"+password);
			
			User user = new User();
			user.setLastName(userLastName);
			user.setFirstName(userFirstName);
			user.setMiddleName(userMiddleName);
			user.setEmailAddress(email);
			user.setUserName(userName);
			user.setPassword(password);			
			user.setStatus(UserStatus.ACTIVE);
			
			em.persist(user);
		}
		
		br.close();
		fr.close();
	}
	
	
	/**
	 * Creates roles and set rights for them
	 * @param filename
	 * @throws Exception
	 */
	public void importRoles(String filename) throws Exception {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);

		while(true) {
			String line = br.readLine();
			if (line == null)
				break;
			String[] tokens = line.split(",");
			String roleName = tokens[0].trim();
			String rightName = tokens[1].trim();
			
			Role role = findRole(roleName);
			if (role == null) {
				role = new Role();
				role.setName(roleName);
				em.persist(role);
			}
			
			try {
				Right right = Right.valueOf(rightName);
				Set<Right> rights = role.getRights();
				if (rights == null) {
					role.setRights(EnumSet.noneOf(Right.class));
					rights = role.getRights();
				}
				rights.add(right);
			} catch (IllegalArgumentException e) {
				//no role for name;
				System.out.println("ERROR : no right for name: " + rightName);
			}
			
		}
		
		br.close();
		fr.close();
	}

	
	/**
	 * Creates user roles
	 * @param filename
	 * @throws Exception
	 */
	public void importUserRoles(String filename) throws Exception {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		while(true) {
			String line = br.readLine();
			if (line == null) break;
			String[] tokens = line.split(",");
			String userName = tokens[0].trim();
			String roleName = tokens[1].trim();
			
			User user = findUserByUserName(userName);
			Role role = findRole(roleName);
			
			if ((user != null) && (role != null)) {
				/*if (user.getRole() == null) {
					user.setRole(new HashSet<Role>());
				}*/
				user.setRole(role);
			}
		}
		
		br.close();
		fr.close();
	}
	
	
	/**
	 * Creates bugs (user, . . .)
	 * @param fileName
	 * @throws Exception
	 */ /*
	public void importBugs(String fileName) throws Exception{
		
	}
	*/
	
	/**
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void importOperatingSystem(String filename) throws Exception {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		
		while(true) {
			String line = br.readLine();
			if (line == null)
				break;
			String[] tokens = line.split(",");
			String name = tokens[0].trim();
			String description = tokens[1].trim();
			
			System.out.println("Line:"+line);
			System.out.println("OS Name:"+name);
			System.out.println("OS Description:"+description);
			
			OperatingSystem op = new OperatingSystem();//name, description);
			op.setName(name);
			op.setDescription(description);
			
			em.persist(op);
		}
		
		br.close();
		fr.close();
	}
	
	/**
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void importPlatform(String filename) throws Exception {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		
		while(true) {
			String line = br.readLine();
			if (line == null)
				break;
			String[] tokens = line.split(",");
			String name = tokens[0].trim();
			String description = tokens[1].trim();
			
			System.out.println("Line:"+line);
			System.out.println("Platform Name:"+name);
			System.out.println("Platform Description:"+description);
			
			Platform p = new Platform(name, description);
			
			em.persist(p);
		}
		
		br.close();
		fr.close();
	}
	
	/**
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void importPriority(String filename) throws Exception {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		
		while(true) {
			String line = br.readLine();
			if (line == null)
				break;
			String[] tokens = line.split(",");
			String name = tokens[0].trim();
			String description = tokens[1].trim();
			
			System.out.println("Line:"+line);
			System.out.println("Priority Name:"+name);
			System.out.println("Priority Description:"+description);
			
			Priority p = new Priority(name, description);
			
			em.persist(p);
		}
		
		br.close();
		fr.close();
	}
	
	/**
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void importSeverity(String filename) throws Exception {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		
		while(true) {
			String line = br.readLine();
			if (line == null)
				break;
			String[] tokens = line.split(",");
			String name = tokens[0].trim();
			String description = tokens[1].trim();
			
			System.out.println("Line:"+line);
			System.out.println("Severity Name:"+name);
			System.out.println("Severity Description:"+description);
			
			Severity s = new Severity(name, description);
			
			em.persist(s);
		}
		
		br.close();
		fr.close();
	}
}