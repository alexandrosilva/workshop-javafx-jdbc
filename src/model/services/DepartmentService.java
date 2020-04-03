package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();

	public List<Department> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Department obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} 
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}
	
	/*
	// Antes de testar no banco de dados vamos MOCAR ou seja testar a funcionalidade
	public List<Department> findAll(){
		List<Department> list = new ArrayList<Department>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Eletronics"));
		return list;		
	}
	*/
}
