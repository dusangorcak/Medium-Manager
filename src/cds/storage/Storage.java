package cds.storage;

/**
 * This class represent Storage (of some mediums). Storage has id(Long), 
 * capacity(int) must be greater than zero and address(String) which can not be null. In one storage could be 
 * one or more mediums (CDs, Books etc.) 
 *
 * 
 * @author Dusan
 */
public class Storage {
    
    private Long id;
    private int capacity;
    private String address;

        
    public Long getId() {
        return id;
    }

  /*  public Storage(Long id, int capacity, String address) {
        this.id = id;
        this.capacity = capacity;
        this.address = address;
    }*/

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setId(Long id) throws IllegalArgumentException {
       if(this.id == null) this.id = id;
       else throw new IllegalArgumentException("id set multiple times");
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

        
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Storage other = (Storage) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}

