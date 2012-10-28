package to.joe.decapitation.datastorage;

import java.sql.SQLException;
import java.util.ArrayList;

import to.joe.decapitation.Bounty;

public interface DataStorageInterface {

    /**
     * Gets the number of uncollected bounties
     * 
     * @return The number of uncollected bounties
     * @throws SQLException
     */
    public int getNumBounties() throws SQLException;

    /**
     * Gets uncollected bounties sorted by highest reward limiting between min and max
     * 
     * @param min
     *            The starting result
     * @param max
     *            The ending result
     * @return Bounties limited between min and max
     * @throws SQLException
     */
    public ArrayList<Bounty> getBounties(int min, int max) throws SQLException;

    /**
     * Gets a list of uncollected bounties that match the specified name sorted A-Z
     * 
     * @param hunted
     *            Name to search for
     * @return Bounties that match the name
     * @throws SQLException
     */
    public ArrayList<Bounty> getBounties(String hunted) throws SQLException;

    /**
     * Gets a list of bounties sorted with the highest reward first
     * 
     * @param issuer
     * @return
     * @throws SQLException
     */
    public ArrayList<Bounty> getOwnBounties(String issuer) throws SQLException;

    /**
     * Gets the bounty with the highest reward with the specified name
     * 
     * @param name
     *            The name to search for
     * @return The highest bounty with the specified name, null if no match
     * @throws SQLException
     */
    public Bounty getBounty(String hunted) throws SQLException;

    /**
     * Gets the bounty with the given name and owner
     * 
     * @param hunted
     * @param issuer
     * @return
     * @throws SQLException
     */
    public Bounty getBounty(String hunted, String issuer) throws SQLException;

    /**
     * Adds the specified bounty to the database
     * 
     * @param bounty
     *            The bounty to add
     * @return The newly added bounty
     * @throws SQLException
     */
    public Bounty addBounty(Bounty bounty) throws SQLException;

    /**
     * Updates the specified bounty in the database
     * 
     * @param bounty
     *            The bounty to update
     * @throws SQLException
     */
    public void updateBounty(Bounty bounty) throws SQLException;

    /**
     * Deletes the specified bounty in the database
     * 
     * @param bounty
     *            The bounty to delete
     * @throws SQLException
     */
    public void deleteBounty(Bounty bounty) throws SQLException;

}
