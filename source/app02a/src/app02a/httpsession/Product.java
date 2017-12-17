package app02a.httpsession;

public class Product {
	private int id ;
	private String name;
	private String description;
	private float prive;
	public Product(int id, String name, String description, float prive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.prive = prive;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getPrive() {
		return prive;
	}
	public void setPrive(float prive) {
		this.prive = prive;
	}
	
	
}
