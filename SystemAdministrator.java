package model;

/**
 * SystemAdministrator: maintains the software and controls access settings.
 */
public class SystemAdministrator extends User {

    private String staffCode;
    private int accessLevel;
    private int lastBackupDay;

    public SystemAdministrator(int userId, String name, String username, String password,
                               String staffCode, int accessLevel) {
        super(userId, name, username, password);
        this.staffCode = staffCode;
        this.accessLevel = accessLevel;
        this.lastBackupDay = 0;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getLastBackupDay() {
        return lastBackupDay;
    }

    public void setLastBackupDay(int lastBackupDay) {
        this.lastBackupDay = lastBackupDay;
    }

    // Operation: access settings (reset the password of any user)
    public void resetPassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }

    // Operation: system status / maintenance
    public String checkSystemStatus(Library library, int today) {
        lastBackupDay = today;
        return "System running normally. Users: " + library.getUserCount()
                + " | Books: " + library.getBookCount()
                + " | Records: " + library.getRecordCount()
                + " | Backup taken on day " + lastBackupDay;
    }

    @Override
    public String getRole() {
        return "System Administrator";
    }

    @Override
    public String getPermissions() {
        return "Maintain system, reset passwords, change access settings";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("   Staff code: " + staffCode + " | Access level: " + accessLevel
                + " | Last backup day: " + lastBackupDay);
    }
}
