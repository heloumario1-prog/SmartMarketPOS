/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Models.CashierReport;
import Models.ReportRow;
import Util.DatabaseConnection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class ReportService {

    DatabaseConnection conn = new DatabaseConnection();

    public double getTotalSalesToday() throws SQLException {
        conn.Connect();
        double amount = 0;
        String sql = "SELECT IFNULL(SUM(final_amount),0) AS AmountToday FROM sales WHERE DATE(date)=CURDATE()";
        try (Statement st = conn.getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                amount = rs.getDouble("AmountToday");
            }
            rs.close();
        }
        conn.Disconnect();
        return amount;
    }

    public double getTotalSalesperDay(LocalDate date) throws SQLException {
        conn.Connect();
        double amount = 0;
        String sql = "SELECT IFNULL(SUM(final_amount),0) AS Amount FROM sales WHERE DATE(date)= " + date;
        try (Statement st = conn.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                amount = rs.getDouble("Amount");
            } else {
                System.out.println("No Sales Found in " + date);
            }
        }
        conn.Disconnect();
        return amount;
    }

    public double getTotalSalesperMonth(int month) throws SQLException {
        conn.Connect();
        double amount = 0;
        String sql = "SELECT IFNULL(SUM(final_amount),0) AS Amount FROM sales WHERE MONTH(date)= " + month;
        if (month > 0 && month < 13) {
            try (Statement st = conn.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    amount = rs.getDouble("Amount");
                } else {
                    System.out.println("No Sales Found in " + month);
                }
            }
        } else {
            System.out.println("Error!!!");
        }
        conn.Disconnect();
        return amount;
    }

    public double getTotalSalesperYear(int year) throws SQLException {
        conn.Connect();
        double amount = 0;
        String sql = "SELECT IFNULL(SUM(final_amount),0) AS Amount FROM sales WHERE YEAR(date)= " + year;
        try (Statement st = conn.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                amount = rs.getDouble("Amount");
            } else {
                System.out.println("No Sales Found in " + year);
            }
        }
        conn.Disconnect();
        return amount;
    }

    public List<CashierReport> getCashierTotalsBetween(LocalDate start, LocalDate end) {
        List<CashierReport> list = new ArrayList<>();
        double totalAll = 0;

        // First get total money for the period
        String totalSql = """
        SELECT IFNULL(SUM(final_amount), 0) AS total
        FROM sales
        WHERE DATE(date) BETWEEN ? AND ?
    """;

        // Then get each cashier’s total
        String cashierSql = """
        SELECT u.username, SUM(s.final_amount) AS total
        FROM sales s
        JOIN users u ON s.cashier_id = u.user_id
        WHERE DATE(s.date) BETWEEN ? AND ?
        GROUP BY s.cashier_id
        ORDER BY total DESC
    """;

        try {
            conn.Connect();

            // Get total sales
            PreparedStatement tps = conn.getConnection().prepareStatement(totalSql);
            tps.setDate(1, Date.valueOf(start));
            tps.setDate(2, Date.valueOf(end));
            ResultSet trs = tps.executeQuery();
            if (trs.next()) {
                totalAll = trs.getDouble("total");
            }

            // Get per-cashier totals
            PreparedStatement cps = conn.getConnection().prepareStatement(cashierSql);
            cps.setDate(1, Date.valueOf(start));
            cps.setDate(2, Date.valueOf(end));

            ResultSet rs = cps.executeQuery();

            while (rs.next()) {
                String cashier = rs.getString("username");
                double total = rs.getDouble("total");

                double percentage = (totalAll == 0) ? 0 : (total / totalAll) * 100;

                list.add(new CashierReport(cashier, total, percentage));
            }

            conn.Disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ReportRow> getSalesBetween(LocalDate start, LocalDate end) {
        List<ReportRow> list = new ArrayList<>();

        String sql = """
        SELECT s.sale_id, s.date, s.final_amount, u.username
        FROM sales s
        JOIN users u ON s.cashier_id = u.user_id
        WHERE DATE(s.date) BETWEEN ? AND ?
        ORDER BY s.date DESC
    """;

        try {
            conn.Connect();

            PreparedStatement ps = conn.getConnection().prepareStatement(sql);
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int saleId = rs.getInt("sale_id");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
                double total = rs.getDouble("final_amount");
                String cashier = rs.getString("username");

                list.add(new ReportRow(saleId, date, total, cashier));
            }

            conn.Disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
