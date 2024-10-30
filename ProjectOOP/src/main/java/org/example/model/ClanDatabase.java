//package org.example.model;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import org.example.menuController.MainMenuController;
//
//public class ClanDatabase {
//
//    public static ArrayList<Clan> getAllClans(Connection conn) throws SQLException {
//        String sqlClans = "SELECT * FROM Clan";
//        String sqlMembers = "SELECT * FROM ClanMembers WHERE clan_id = ?";
//
//        ArrayList<Clan> clans = new ArrayList<>();
//
//        try (PreparedStatement stmtClans = conn.prepareStatement(sqlClans)) {
//            try (ResultSet rsClans = stmtClans.executeQuery()) {
//                while (rsClans.next()) {
//                    int clanId = rsClans.getInt("clan_id");
//                    String name = rsClans.getString("name");
//                    int won = rsClans.getInt("won");
//                    int lost = rsClans.getInt("lost");
//                    int points = rsClans.getInt("points");
//
//                    ArrayList<User> members = new ArrayList<>();
//
//                    try (PreparedStatement stmtMembers = conn.prepareStatement(sqlMembers)) {
//                        stmtMembers.setInt(1, clanId);
//                        try (ResultSet rsMembers = stmtMembers.executeQuery()) {
//                            while (rsMembers.next()) {
//                                // Assume the User class has a constructor that takes a username
//                                String username = rsMembers.getString("username");
//                                User member = User.fetchByUsername(conn, username);
//                                members.add(member);
//                            }
//                        }
//                    }
//
//                    Clan clan = new Clan(members, name, points, lost, won);
//                    clans.add(clan);
//                }
//            }
//        }
//
//        return clans;
//    }
//
//    public static void addMemberToClan(Connection conn, String name, User user) throws SQLException {
//        String sqlAddMember = "INSERT INTO ClanMembers (clan_id, username) VALUES (?, ?)";
//        String sqlGetClanId = "SELECT clan_id FROM Clan WHERE name = ?";
//
//        int clanId = 0;
//        try (PreparedStatement stmtGetClanId = conn.prepareStatement(sqlGetClanId)) {
//
//            stmtGetClanId.setString(1, name);
//            try (ResultSet rs = stmtGetClanId.executeQuery()) {
//                if (rs.next()) {
//                    clanId = rs.getInt("clan_id");
//                }
//            }
//        }
//
//        if (clanId > 0) {
//            try (PreparedStatement stmtAddMember = conn.prepareStatement(sqlAddMember)) {
//                stmtAddMember.setInt(1, clanId);
//                stmtAddMember.setString(2, user.getUsername());
//                stmtAddMember.executeUpdate();
//            }
//        }
//    }
//
//    public static boolean doesClanExist(Connection conn, String clanName) throws SQLException {
//        String sql = "SELECT 1 FROM Clan WHERE name = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, clanName);
//            try (ResultSet rs = stmt.executeQuery()) {
//                return rs.next();  // If there's at least one result, the clan exists
//            }
//        }
//    }
//
//    public static void addClan(Connection conn, Clan clan) throws SQLException {
//        String sqlInsertClan = "INSERT INTO Clan (name, code, won, lost, points) VALUES (?, ?, ?, ?, ?)";
//        String sqlInsertMember = "INSERT INTO ClanMembers (clan_id, username) VALUES (?, ?)";
//
//        try (PreparedStatement stmtInsertClan = conn.prepareStatement(sqlInsertClan, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            stmtInsertClan.setString(1, clan.getName());
//            stmtInsertClan.setString(2, clan.getCode());
//            stmtInsertClan.setInt(3, 0); // Initial wins
//            stmtInsertClan.setInt(4, 0); // Initial losses
//            stmtInsertClan.setInt(5, 0); // Initial points
//
//            int affectedRows = stmtInsertClan.executeUpdate();
//
//            if (affectedRows == 0) {
//                throw new SQLException("Creating clan failed, no rows affected.");
//            }
//
//            try (ResultSet generatedKeys = stmtInsertClan.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    int clanId = generatedKeys.getInt(1);
//
//                    try (PreparedStatement stmtInsertMember = conn.prepareStatement(sqlInsertMember)) {
//                        stmtInsertMember.setInt(1, clanId);
//                        stmtInsertMember.setString(2, clan.getBoss().getUsername());
//                        stmtInsertMember.executeUpdate();
//                    }
//                } else {
//                    throw new SQLException("Creating clan failed, no ID obtained.");
//                }
//            }
//        }
//    }
//
//    public static Clan getClanByPlayerUsername(Connection conn, String username) throws SQLException {
//        String sql = "SELECT c.* " +
//                "FROM Clan c " +
//                "JOIN ClanMembers cm ON c.clan_id = cm.clan_id " +
//                "WHERE cm.username = ?";
//
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, username);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    String clanName = rs.getString("name");
//                    int wins = rs.getInt("won");
//                    int losses = rs.getInt("lost");
//                    int points = rs.getInt("points");
//
//                    // Assuming you have a constructor in Clan class to initialize the clan
//                    return new Clan(clanName, points, losses, wins); // Replace null with boss if available
//                } else {
//                    return null; // Player is not in any clan
//                }
//            }
//        }
//    }
//
//}
//
