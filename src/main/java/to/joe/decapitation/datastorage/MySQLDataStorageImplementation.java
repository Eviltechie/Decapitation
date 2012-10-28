package to.joe.decapitation.datastorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import to.joe.decapitation.Bounty;
import to.joe.decapitation.Decapitation;

public class MySQLDataStorageImplementation implements DataStorageInterface {
    
    Decapitation plugin;
    
    public MySQLDataStorageImplementation(Decapitation decapitation) {
        plugin = decapitation;
    }

    @Override
    public int getNumBounties() throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("SELECT count(*) FROM bounties WHERE hunter IS NULL");
        ResultSet rs = ps.executeQuery();
        return rs.getInt(1);
    }

    @Override
    public ArrayList<Bounty> getBounties(int min, int max) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE hunter IS NULL ORDER BY bounties.reward DESC LIMIT 0,9");
        ResultSet rs = ps.executeQuery();
        ArrayList<Bounty> bounties = new ArrayList<Bounty>();
        while (rs.next()) {
            bounties.add(new Bounty(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getTimestamp(5), rs.getString(6), rs.getTimestamp(7), rs.getTimestamp(8)));
        }
        return bounties;
    }

    @Override
    public Bounty getBounty(String hunted) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE hunted LIKE ? AND hunter IS NULL ORDER BY bounties.reward DESC LIMIT 1");
        ps.setString(1, hunted);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Bounty(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getTimestamp(5), rs.getString(6), rs.getTimestamp(7), rs.getTimestamp(8));
        } else {
            return null;
        }
    }

    @Override
    public Bounty addBounty(Bounty bounty) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementWithGeneratedKeys("INSERT INTO bounties (issuer, hunted, reward) VALUES (?,?,?)");
        ps.setString(1, bounty.getIssuer());
        ps.setString(2, bounty.getHunted());
        ps.setDouble(3, bounty.getReward());
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE id = ?");
        ps.setInt(1, rs.getInt(1));
        rs = ps.executeQuery();
        rs.next();
        return new Bounty(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getTimestamp(5), rs.getString(6), rs.getTimestamp(7), rs.getTimestamp(8));
    }

    @Override
    public void updateBounty(Bounty bounty) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("UPDATE bounties SET issuer = ?, hunted = ?, reward = ?, created = ?, hunter = ?, turnedin = ? redeemed = ?");
        ps.setString(1, bounty.getIssuer());
        ps.setString(2, bounty.getHunted());
        ps.setDouble(3, bounty.getReward());
        ps.setTimestamp(4, bounty.getCreated());
        if (bounty.getHunter() == null)
            ps.setNull(5, Types.VARCHAR);
        else
            ps.setString(5, bounty.getHunter());
        
        if (bounty.getTurnedIn() == null)
            ps.setNull(6, Types.TIMESTAMP);
        else
            ps.setTimestamp(6, bounty.getTurnedIn());
        
        if (bounty.getRedeemed() == null)
            ps.setNull(7, Types.TIMESTAMP);
        else
            ps.setTimestamp(7, bounty.getRedeemed());
        
        ps.execute();
    }

    @Override
    public void deleteBounty(Bounty bounty) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("DELETE FROM bounties WHERE id = ?");
        ps.setInt(1, bounty.getID());
        ps.execute();   
    }

    @Override
    public ArrayList<Bounty> getBounties(String hunted) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE hunted LIKE ? AND hunter IS NULL ORDER BY bounties.hunted ASC");
        ps.setString(1, "%" + hunted + "%");
        ResultSet rs = ps.executeQuery();
        ArrayList<Bounty> bounties = new ArrayList<Bounty>();
        while (rs.next()) {
            bounties.add(new Bounty(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getTimestamp(5), rs.getString(6), rs.getTimestamp(7), rs.getTimestamp(8)));
        }
        return bounties;
    }

    @Override
    public Bounty getBounty(String hunted, String issuer) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE hunted LIKE ? AND ISSUER LIKE ? AND hunter IS NULL ORDER BY bounties.reward DESC LIMIT 1");
        ps.setString(1, hunted);
        ps.setString(2, issuer);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Bounty(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getTimestamp(5), rs.getString(6), rs.getTimestamp(7), rs.getTimestamp(8));
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Bounty> getOwnBounties(String issuer) throws SQLException {
        PreparedStatement ps = plugin.getSQL().getFreshPreparedStatementColdFromTheRefrigerator("SELECT * FROM bounties WHERE issuer LIKE ? AND hunter IS NULL ORDER BY bounties.reward DESC");
        ps.setString(1, issuer);
        ResultSet rs = ps.executeQuery();
        ArrayList<Bounty> bounties = new ArrayList<Bounty>();
        while (rs.next()) {
            bounties.add(new Bounty(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getTimestamp(5), rs.getString(6), rs.getTimestamp(7), rs.getTimestamp(8)));
        }
        return bounties;
    }

}
