package cds.storage;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Tomas
 */
public class StorageManagerImpl implements StorageManager {

    public static final Logger logger = Logger.getLogger(StorageManagerImpl.class.getName());
    private Connection conn;
    
    StorageManagerImpl(Connection conn){
        this.conn = conn;
    }

    public StorageManagerImpl() {
        
    }
    
    
    @Override
    public void createStorage(Storage store) throws IllegalArgumentException {
        if(store == null){
            throw new IllegalArgumentException("Store is null");
        }        
        if(store.getId() != null){
            throw new IllegalArgumentException("Store has allready set id");
        }        
        if(store.getCapacity() <= 0){
            throw new IllegalArgumentException("Store capaciry is less than 0");
        }        
        if(store.getAddress() == null){
            throw new IllegalArgumentException("Address in null");
        }
        
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO STORAGE(CAPACITY,ADDRESS) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, store.getCapacity());
            st.setString(2, store.getAddress());
            int addedRows = st.executeUpdate();
            if(addedRows != 1){
                throw new RunTimeFailureException("Error:  creating storage - " + store);
            }
            
            ResultSet rs = st.getGeneratedKeys();
            store.setId(getKey(rs,store));
        } catch(SQLException ex){
            throw new RunTimeFailureException("Error: inserting storage - " + store,ex);
        } finally{
            if(st != null){
                try{
                    st.close();
                } catch(SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private Long getKey(ResultSet rs,Storage store) throws SQLException,RunTimeFailureException{
        if(rs.next()){
            if(rs.getMetaData().getColumnCount() != 1){
                throw new RunTimeFailureException("Error: generating keys -  wrong keys count " + store);
            }
            Long key = rs.getLong(1);
            if(rs.next()){
                throw new RunTimeFailureException("Error: generating keys - more keys found " + store);
            }
            return key;
        } else {
            throw new RunTimeFailureException("Error: generating keys - no key found " + store );
        }
    }

    @Override
    public void deleteStorage(Storage store) throws IllegalArgumentException{
        if(store == null){
            throw new IllegalArgumentException("Store is null");
        }  
        if(store.getId() == null){
            throw new IllegalArgumentException("id is null");
        }        
        if(store.getAddress() == null){
            throw new IllegalArgumentException("address is null");
        }        
                                
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM storage WHERE id=?");
            st.setLong(1, store.getId());
            int deletedStores = st.executeUpdate();
            if(deletedStores != 1){
                throw new RunTimeFailureException("Error:  deleted can be only one storage" + store);
            }
        
            
        } catch(SQLException ex){
            throw new RunTimeFailureException("Error: deleting storage - " + store,ex);
        } finally{
            if(st != null){
                try{
                    st.close();
                } catch(SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public Storage getStorage(Long id) throws IllegalArgumentException {
           
        if(id == null){
            throw new IllegalArgumentException("Id is null");
        } 
        
        if(id <= 0){
            throw new IllegalArgumentException("Id must be greather than zero.");
        }
        
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement
                    ("SELECT id,capacity,address FROM STORAGE WHERE id=?");
            st.setLong(1,id);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Storage store = resultSetToStorage(rs);

                if (rs.next()) {
                    throw new RunTimeFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + store + " and " + resultSetToStorage(rs));                    
                }            
                
                return store;
            } else {
                return null;
            }
            
        } catch (SQLException ex) {
            throw new RunTimeFailureException(
                    "Error when retrieving storage with id " + id, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        }
    

    private Storage resultSetToStorage(ResultSet rs) throws SQLException {
        Storage store = new Storage();
        store.setId(rs.getLong("id"));
        store.setCapacity(rs.getInt("capacity"));
        store.setAddress(rs.getString("address"));
        return store;
    }

    @Override
    public void updateStorage(Storage store) throws IllegalArgumentException {
        if(store == null){
            throw new IllegalArgumentException("Store is null");
        }  
        
        if(store.getId() == null){
            throw new IllegalArgumentException("Store is null");
        }  
              
        if(store.getCapacity() <= 0){
            throw new IllegalArgumentException("Store capaciry is less than 0");
        }        
        if(store.getAddress() == null){
            throw new IllegalArgumentException("Address in null");
        }
        
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement
                    ("UPDATE storage SET capacity=?, address=?"
                            + " WHERE id=?");
            st.setLong(3,store.getId());
            st.setInt(1, store.getCapacity());
            st.setString(2, store.getAddress());
            int updated = st.executeUpdate();
            if(updated > 2){
                throw new RunTimeFailureException("Error: updated must be at least two attributes" + store);
            }
            
        
        } catch(SQLException ex){
            throw new RunTimeFailureException("Error: deleting storage - " + store,ex);
        } finally{
            if(st != null){
                try{
                    st.close();
                } catch(SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
}