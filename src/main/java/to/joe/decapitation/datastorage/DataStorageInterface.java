package to.joe.decapitation.datastorage;

import java.util.List;

import to.joe.decapitation.Bounty;

public interface DataStorageInterface {

    /**
     * Gets the number of uncollected bounties
     * 
     * @return The number of uncollected bounties
     * @throws DataStorageException
     */
    public int getNumBounties() throws DataStorageException;

    /**
     * Gets the number of unclaimed heads an issuer has
     * 
     * @param issuer
     * @return
     * @throws DataStorageException
     */
    public int getNumUnclaimedHeads(String issuer) throws DataStorageException;

    /**
     * Gets a list of bounties that are unclaimed by the issuer
     * 
     * @param issuer
     * @return
     * @throws DataStorageException
     */
    public List<Bounty> getUnclaimedBounties(String issuer) throws DataStorageException;

    /**
     * Gets uncollected bounties sorted by highest reward limiting between min and max
     * 
     * @param min
     *            The starting result
     * @param max
     *            The ending result
     * @return Bounties limited between min and max
     * @throws DataStorageException
     */
    public List<Bounty> getBounties(int min, int max) throws DataStorageException;

    /**
     * Gets a list of uncollected bounties that match the specified name sorted A-Z
     * 
     * @param hunted
     *            Name to search for
     * @return Bounties that match the name
     * @throws DataStorageException
     */
    public List<Bounty> getBounties(String hunted) throws DataStorageException;

    /**
     * Gets a list of bounties sorted with the highest reward first
     * 
     * @param issuer
     * @return
     * @throws DataStorageException
     */
    public List<Bounty> getOwnBounties(String issuer) throws DataStorageException;

    /**
     * Gets the bounty with the highest reward with the specified name
     * 
     * @param name
     *            The name to search for
     * @return The highest bounty with the specified name, null if no match
     * @throws DataStorageException
     */
    public Bounty getBounty(String hunted) throws DataStorageException;

    /**
     * Gets the bounty with the given name and owner
     * 
     * @param hunted
     * @param issuer
     * @return
     * @throws DataStorageException
     */
    public Bounty getBounty(String hunted, String issuer) throws DataStorageException;

    /**
     * Adds the specified bounty to the database
     * 
     * @param bounty
     *            The bounty to add
     * @return The newly added bounty
     * @throws DataStorageException
     */
    public Bounty addBounty(Bounty bounty) throws DataStorageException;

    /**
     * Updates the specified bounty in the database
     * 
     * @param bounty
     *            The bounty to update
     * @throws DataStorageException
     */
    public void updateBounty(Bounty bounty) throws DataStorageException;

    /**
     * Deletes the specified bounty in the database
     * 
     * @param bounty
     *            The bounty to delete
     * @throws DataStorageException
     */
    public void deleteBounty(Bounty bounty) throws DataStorageException;

}
