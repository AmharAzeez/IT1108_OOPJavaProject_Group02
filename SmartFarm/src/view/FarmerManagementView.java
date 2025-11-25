package view;/*
 * FarmerNetworkApp.java
 * Single-file Java Swing app implementing a simplified Farmer Network Management system.
 * Includes in-memory models, service, controller and a Swing UI with tabs to:
 *  - Register / Edit / Delete farmers (id, name, contact, location, size)
 *  - Add certifications and specializations (comma-separated)
 *  - Add farms for a selected farmer (location, size, sustainable practices)
 *  - Add performance metrics
 *  - Simple skill-sharing program creation and enrollment
 *  - Simple mentorship matching by specialization
 *
 * Diagram reference (uploaded by user):
 * /mnt/data/A_UML_(Unified_Modeling_Language)_use_case_diagram.png
 *
 * To compile & run:
 * javac FarmerNetworkApp.java
 * java FarmerNetworkApp
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class FarmerManagementView extends JFrame {
    private final FarmerController controller = new FarmerController(new FarmerService());

    // UI components reused
    private final DefaultListModel<String> farmerListModel = new DefaultListModel<>();
    private final JList<String> farmerList = new JList<>(farmerListModel);

    public FarmerManagementView() {
        super("Farmer Network Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Farmers", buildFarmerPanel());
        tabs.addTab("Farms", buildFarmPanel());
        tabs.addTab("Programs", buildProgramPanel());
        tabs.addTab("Metrics", buildMetricsPanel());
        tabs.addTab("Mentorship", buildMentorshipPanel());

        add(tabs, BorderLayout.CENTER);

        // refresh list on window open
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) { refreshFarmerList(); }
        });
    }

    // ---------- Panels ----------
    private JPanel buildFarmerPanel() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Register / Update Farmer"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        JTextField idField = new JTextField(12);
        JTextField nameField = new JTextField(18);
        JTextField contactField = new JTextField(12);
        JTextField locationField = new JTextField(12);
        JTextField sizeField = new JTextField(6);
        JTextField specsField = new JTextField(20);
        JTextField certsField = new JTextField(20);

        int row=0;
        c.gridx=0; c.gridy=row; form.add(new JLabel("ID:"), c);
        c.gridx=1; form.add(idField, c);
        c.gridx=2; form.add(new JLabel("Name:"), c);
        c.gridx=3; form.add(nameField, c);
        row++;
        c.gridx=0; c.gridy=row; form.add(new JLabel("Contact:"), c);
        c.gridx=1; form.add(contactField, c);
        c.gridx=2; form.add(new JLabel("Location:"), c);
        c.gridx=3; form.add(locationField, c);
        row++;
        c.gridx=0; c.gridy=row; form.add(new JLabel("Farm size (ha):"), c);
        c.gridx=1; form.add(sizeField, c);
        c.gridx=2; form.add(new JLabel("Specializations (comma):"), c);
        c.gridx=3; form.add(specsField, c);
        row++;
        c.gridx=0; c.gridy=row; form.add(new JLabel("Certifications (comma):"), c);
        c.gridx=1; c.gridwidth=3; form.add(certsField, c); c.gridwidth=1;
        row++;

        JButton registerBtn = new JButton("Save / Register");
        JButton clearBtn = new JButton("Clear");
        JButton deleteBtn = new JButton("Delete Selected");

        JPanel btnp = new JPanel(); btnp.add(registerBtn); btnp.add(clearBtn); btnp.add(deleteBtn);
        c.gridx=0; c.gridy=row; c.gridwidth=4; form.add(btnp, c); c.gridwidth=1;

        // Right side list
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(new TitledBorder("Farmers"));
        farmerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        right.add(new JScrollPane(farmerList), BorderLayout.CENTER);

        // When selecting a farmer, populate fields
        farmerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String sel = farmerList.getSelectedValue();
                if (sel == null) return;
                String id = sel.split(" - ")[0];
                Farmer f = controller.find(id);
                if (f != null) {
                    idField.setText(f.id);
                    idField.setEnabled(false);
                    nameField.setText(f.name);
                    contactField.setText(f.contact == null ? "" : f.contact);
                    locationField.setText(f.primaryLocation == null ? "" : f.primaryLocation);
                    sizeField.setText(f.primaryFarmSize==null?"":String.valueOf(f.primaryFarmSize));
                    specsField.setText(String.join(",", f.specializations));
                    certsField.setText(String.join(",", f.certifications));
                }
            }
        });

        registerBtn.addActionListener(ev -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (id.isEmpty() || name.isEmpty()) { JOptionPane.showMessageDialog(this, "ID and Name required"); return; }
            Farmer f = new Farmer(id, name);
            f.contact = contactField.getText().trim();
            f.primaryLocation = locationField.getText().trim();
            try { String s = sizeField.getText().trim(); if (!s.isEmpty()) f.primaryFarmSize = Double.parseDouble(s); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid size number"); return; }
            f.specializations = parseList(specsField.getText());
            f.certifications = parseList(certsField.getText());

            controller.save(f);
            refreshFarmerList();
            clearFields(idField, nameField, contactField, locationField, sizeField, specsField, certsField);
            idField.setEnabled(true);
        });

        clearBtn.addActionListener(ev -> clearFields(idField, nameField, contactField, locationField, sizeField, specsField, certsField));

        deleteBtn.addActionListener(ev -> {
            String sel = farmerList.getSelectedValue(); if (sel==null) { JOptionPane.showMessageDialog(this, "Select a farmer first"); return; }
            String id = sel.split(" - ")[0];
            int ok = JOptionPane.showConfirmDialog(this, "Delete farmer " + id + "?","Confirm", JOptionPane.YES_NO_OPTION);
            if (ok==JOptionPane.YES_OPTION) { controller.delete(id); refreshFarmerList(); }
        });

        root.add(form, BorderLayout.CENTER);
        root.add(right, BorderLayout.EAST);
        return root;
    }

    private JPanel buildFarmPanel() {
        JPanel p = new JPanel(new BorderLayout(8,8));
        p.setBorder(new TitledBorder("Manage Farms"));

        DefaultListModel<String> farmListModel = new DefaultListModel<>();
        JList<String> farmsList = new JList<>(farmListModel);

        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        JTextField farmerIdField = new JTextField(10);
        JTextField locationField = new JTextField(12);
        JTextField sizeField = new JTextField(6);
        JTextField practicesField = new JTextField(20);
        JButton addFarmBtn = new JButton("Add Farm");
        JButton viewFarmsBtn = new JButton("View Selected Farmer's Farms");

        form.add(new JLabel("Farmer ID:")); form.add(farmerIdField);
        form.add(new JLabel("Location:")); form.add(locationField);
        form.add(new JLabel("Size (ha):")); form.add(sizeField);
        form.add(new JLabel("Sustainable practices (comma):")); form.add(practicesField);
        form.add(new JLabel()); form.add(addFarmBtn);
        form.add(new JLabel()); form.add(viewFarmsBtn);

        addFarmBtn.addActionListener(e -> {
            String fid = farmerIdField.getText().trim();
            if (fid.isEmpty()) { JOptionPane.showMessageDialog(this, "Farmer ID required"); return; }
            Farmer f = controller.find(fid); if (f==null) { JOptionPane.showMessageDialog(this, "Farmer not found"); return; }
            String loc = locationField.getText().trim();
            Double sz = null; try { if (!sizeField.getText().trim().isEmpty()) sz = Double.parseDouble(sizeField.getText().trim()); } catch (NumberFormatException ex){ JOptionPane.showMessageDialog(this, "Invalid size"); return; }
            List<String> practices = parseList(practicesField.getText());
            Farm farm = new Farm(UUID.randomUUID().toString(), loc, sz, practices);
            controller.addFarm(fid, farm);
            JOptionPane.showMessageDialog(this, "Farm added to farmer " + fid);
            farmerIdField.setText(""); locationField.setText(""); sizeField.setText(""); practicesField.setText("");
        });

        viewFarmsBtn.addActionListener(e -> {
            farmListModel.clear();
            String sel = farmerList.getSelectedValue();
            String id;
            if (sel!=null) id = sel.split(" - ")[0]; else id = JOptionPane.showInputDialog(this, "Enter farmer ID to view farms:");
            if (id==null || id.trim().isEmpty()) return;
            Farmer f = controller.find(id.trim()); if (f==null) { JOptionPane.showMessageDialog(this, "Farmer not found"); return; }
            for (Farm farm : f.farms) farmListModel.addElement(farm.id + " - " + farm.location + " (" + (farm.size==null?"?":farm.size)+" ha)");
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(farmsList), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildProgramPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setBorder(new TitledBorder("Skill-share Programs"));

        DefaultListModel<String> progModel = new DefaultListModel<>();
        JList<String> progList = new JList<>(progModel);

        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        JTextField titleField = new JTextField(18);
        JTextArea descArea = new JTextArea(4,20);
        JTextField participantsField = new JTextField(30); // comma of farmer ids
        JButton createBtn = new JButton("Create Program");
        JButton enrollBtn = new JButton("Enroll Selected Farmers (by ID list)");

        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Description:")); form.add(new JScrollPane(descArea));
        form.add(new JLabel("Participant IDs (comma):")); form.add(participantsField);
        form.add(createBtn); form.add(enrollBtn);

        createBtn.addActionListener(e -> {
            String title = titleField.getText().trim(); if (title.isEmpty()) { JOptionPane.showMessageDialog(this, "Title required"); return; }
            SkillShareProgram prog = controller.createProgram(title, descArea.getText().trim());
            progModel.addElement(prog.id + " - " + prog.title);
            titleField.setText(""); descArea.setText(""); participantsField.setText("");
        });

        enrollBtn.addActionListener(e -> {
            String entry = participantsField.getText().trim(); if (entry.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter participant IDs"); return; }
            String[] parts = entry.split(",");
            String sel = progList.getSelectedValue();
            if (sel==null) { JOptionPane.showMessageDialog(this, "Select a program from list after creating it"); return; }
            String pid = sel.split(" - ")[0];
            for (String s : parts) { controller.enrollProgram(pid, s.trim()); }
            JOptionPane.showMessageDialog(this, "Enrolled participants (some may have failed if id not found)");
            participantsField.setText("");
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(progList), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMetricsPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setBorder(new TitledBorder("Performance Metrics"));
        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        JTextField fidField = new JTextField(10);
        JTextField typeField = new JTextField(12);
        JTextField valueField = new JTextField(8);
        JButton addBtn = new JButton("Add Metric");
        DefaultListModel<String> outModel = new DefaultListModel<>();
        JList<String> outList = new JList<>(outModel);

        form.add(new JLabel("Farmer ID:")); form.add(fidField);
        form.add(new JLabel("Metric Type:")); form.add(typeField);
        form.add(new JLabel("Value:")); form.add(valueField);
        form.add(new JLabel()); form.add(addBtn);
        addBtn.addActionListener(e -> {
            String fid = fidField.getText().trim(); if (fid.isEmpty()) { JOptionPane.showMessageDialog(this, "Farmer ID required"); return; }
            Farmer f = controller.find(fid); if (f==null) { JOptionPane.showMessageDialog(this, "Farmer not found"); return; }
            String type = typeField.getText().trim(); double val; try { val = Double.parseDouble(valueField.getText().trim()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid number"); return; }
            PerformanceMetric m = new PerformanceMetric(UUID.randomUUID().toString(), fid, type, val, LocalDateTime.now());
            controller.addMetric(fid, m);
            outModel.addElement(m.metricType + ": " + m.value + " @ " + m.recordedAt);
            fidField.setText(""); typeField.setText(""); valueField.setText("");
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(outList), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMentorshipPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setBorder(new TitledBorder("Mentorship Matching"));
        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        JTextField specField = new JTextField(20);
        JButton matchBtn = new JButton("Find mentors for specialization");
        DefaultListModel<String> outModel = new DefaultListModel<>();
        JList<String> outList = new JList<>(outModel);

        form.add(new JLabel("Specialization:")); form.add(specField);
        form.add(new JLabel()); form.add(matchBtn);

        matchBtn.addActionListener(e -> {
            outModel.clear(); String spec = specField.getText().trim(); if (spec.isEmpty()) return;
            List<Farmer> mentors = controller.findBySpecialization(spec);
            if (mentors.isEmpty()) outModel.addElement("No mentors found for: " + spec);
            else mentors.forEach(f -> outModel.addElement(f.id + " - " + f.name + " (specs: " + String.join(",", f.specializations) + ")"));
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(outList), BorderLayout.CENTER);
        return p;
    }

    // ---------- Helpers ----------
    private void refreshFarmerList() {
        farmerListModel.clear();
        for (Farmer f : controller.listAll()) farmerListModel.addElement(f.id + " - " + f.name);
    }

    private static void clearFields(JTextField... fields) { for (JTextField f : fields) f.setText(""); }

    private static List<String> parseList(String raw) {
        if (raw == null || raw.trim().isEmpty()) return new ArrayList<>();
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FarmerManagementView app = new FarmerManagementView();
            app.setVisible(true);
        });
    }

    // ---------- Domain & Service classes (simple in-file implementations) ----------
    static class Farmer {
        String id;
        String name;
        String contact;
        String primaryLocation;
        Double primaryFarmSize;
        List<String> specializations = new ArrayList<>();
        List<String> certifications = new ArrayList<>();
        List<Farm> farms = new ArrayList<>();
        List<PerformanceMetric> metrics = new ArrayList<>();

        Farmer(String id, String name) { this.id = id; this.name = name; }

        @Override public String toString() { return id + " - " + name; }
    }

    static class Farm {
        String id; String location; Double size; List<String> sustainablePractices = new ArrayList<>();
        Farm(String id, String location, Double size, List<String> practices){ this.id=id; this.location=location; this.size=size; if (practices!=null)this.sustainablePractices.addAll(practices);}    }

    static class PerformanceMetric {
        String metricId; String farmerId; String metricType; double value; LocalDateTime recordedAt;
        PerformanceMetric(String metricId, String farmerId, String metricType, double value, LocalDateTime recordedAt){this.metricId=metricId;this.farmerId=farmerId;this.metricType=metricType;this.value=value;this.recordedAt=recordedAt;}
    }

    static class SkillShareProgram {
        String id; String title; String description; List<String> participants = new ArrayList<>();
        SkillShareProgram(String id, String title, String desc){this.id=id;this.title=title;this.description=desc;}
    }

    // ---------- Service / Controller / Repository (simple in-memory)
    static class FarmerRepository {
        private final Map<String, Farmer> map = new HashMap<>();
        List<Farmer> findAll(){ return new ArrayList<>(map.values()); }
        Optional<Farmer> findById(String id){ return Optional.ofNullable(map.get(id)); }
        void save(Farmer f){ map.put(f.id, f); }
        void delete(String id){ map.remove(id); }
    }

    static class FarmerService {
        private final FarmerRepository repo = new FarmerRepository();
        private final Map<String, SkillShareProgram> programs = new HashMap<>();

        List<Farmer> listAll() { return repo.findAll(); }
        Optional<Farmer> find(String id) { return repo.findById(id); }
        void save(Farmer f) { repo.save(f); }
        void delete(String id) { repo.delete(id); }
        void addFarm(String farmerId, Farm farm){ repo.findById(farmerId).ifPresent(f -> f.farms.add(farm)); }
        void addMetric(String farmerId, PerformanceMetric m){ repo.findById(farmerId).ifPresent(f -> f.metrics.add(m)); }
        SkillShareProgram createProgram(String title, String desc){ SkillShareProgram p = new SkillShareProgram(UUID.randomUUID().toString(), title, desc); programs.put(p.id,p); return p; }
        void enrollProgram(String pid, String farmerId){ SkillShareProgram p = programs.get(pid); if (p!=null && repo.findById(farmerId).isPresent()) p.participants.add(farmerId); }
        List<Farmer> findBySpecialization(String spec){ return repo.findAll().stream().filter(f -> f.specializations.stream().anyMatch(s -> s.equalsIgnoreCase(spec))).collect(Collectors.toList()); }
        Optional<SkillShareProgram> getProgram(String id){ return Optional.ofNullable(programs.get(id)); }
    }

    static class FarmerController {
        private final FarmerService service;
        FarmerController(FarmerService service){ this.service = service; }
        List<Farmer> listAll(){ return service.listAll(); }
        Farmer find(String id){ return service.find(id).orElse(null); }
        void save(Farmer f){ // upsert
            // merge if exists
            Farmer existing = service.find(f.id).orElse(null);
            if (existing!=null){
                existing.name = f.name; existing.contact = f.contact; existing.primaryLocation = f.primaryLocation; existing.primaryFarmSize = f.primaryFarmSize;
                existing.specializations = new ArrayList<>(f.specializations); existing.certifications = new ArrayList<>(f.certifications);
                service.save(existing);
            } else service.save(f);
        }
        void delete(String id){ service.delete(id); }
        void addFarm(String fid, Farm farm){ service.addFarm(fid, farm); }
        void addMetric(String fid, PerformanceMetric m){ service.addMetric(fid, m); }
        SkillShareProgram createProgram(String title, String desc){ return service.createProgram(title, desc); }
        void enrollProgram(String pid, String fid){ service.enrollProgram(pid, fid); }
        List<Farmer> findBySpecialization(String spec){ return service.findBySpecialization(spec); }
    }
}
