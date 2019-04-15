package org.qiankai.window;

import org.qiankai.algorithm.AlphaNumComparator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ChooseDictionary extends JFrame {
    private static final long serialVersionUID = 116835049430402866L;
    private String defaultPath;

    public static void main(String[] args) {
        new ChooseDictionary();
    }

    public ChooseDictionary() {
        String font = "΢���ź�";
        this.defaultPath = new File("").getAbsolutePath(); // Ĭ��ָ��ǰ·��
        // ����----------------------------------------
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(ChooseDictionary.class.getResource("/org/qiankai/jpg/32.jpg"))); // ͼ��
        this.setTitle("�ļ��б���"); // ����
        this.setSize(400, 240); // �ߴ�
        this.setLocationRelativeTo(null); // ��λ����Ļ����
        this.setVisible(true); // ���ڿɼ�
        this.setResizable(false); // ���ɸı䴰�ڳߴ�
        this.getContentPane().setLayout(null); // �����޲��ַ�ʽ

        { // ����----------------------------------------
            Label titleLabel = new Label("�ļ��б���");
            titleLabel.setAlignment(Label.CENTER);
            titleLabel.setFont(new Font(font, Font.PLAIN, 28));
            titleLabel.setBounds(10, 10, 374, 50);
            this.getContentPane().add(titleLabel);
        }

        // �Ƿ�ֻ��ʾ�ļ���----------------------------------------
        Checkbox folderOnlyCheckbox = new Checkbox("ֻ��ʾ�ļ���");
        folderOnlyCheckbox.setBounds(86, 129, 100, 23);
        this.getContentPane().add(folderOnlyCheckbox);
        folderOnlyCheckbox.setFocusable(false);

        // �������Ƿ���ļ���----------------------------------------
        Checkbox openFolderCheckbox = new Checkbox("��������ļ���");
        openFolderCheckbox.setBounds(210, 129, 115, 23);
        this.getContentPane().add(openFolderCheckbox);
        openFolderCheckbox.setFocusable(false);

        // Դ�ļ��в���----------------------------------------
        int sourceYyLocation = 70;
        int sourceHeight = 23;

        // �ı���ʾ
        Label sourcePathLabel = new Label("Դ�ļ���");
        sourcePathLabel.setFont(new Font(font, Font.PLAIN, 12));
        sourcePathLabel.setBounds(10, sourceYyLocation, 69, sourceHeight);
        this.getContentPane().add(sourcePathLabel);
        // �ļ�·������
        TextField sourcePathTextField = new TextField(this.defaultPath);
        sourcePathTextField.setBounds(86, sourceYyLocation, 240, sourceHeight);
        this.getContentPane().add(sourcePathTextField);
        // ѡ��·����ť
        Button sourcePathButton = new Button("...");
        sourcePathButton.setBounds(340, sourceYyLocation, 40, sourceHeight);
        sourcePathButton.setFocusable(false);
        sourcePathButton.addActionListener(event->this.setPath(sourcePathTextField, sourcePathTextField.getText(), folderOnlyCheckbox.getState()));
        this.getContentPane().add(sourcePathButton);


        // �����ļ��в���----------------------------------------
        int exportYLocation = 100;
        int exportHeight = 23;

        // �ı���ʾ
        Label exportPathLabel = new Label("�����ļ���");
        exportPathLabel.setFont(new Font(font, Font.PLAIN, 12));
        exportPathLabel.setBounds(10, exportYLocation, 69, exportHeight);
        this.getContentPane().add(exportPathLabel);
        // �ļ�·������
        TextField exportPathTextField = new TextField(this.defaultPath);
        exportPathTextField.setBounds(86, exportYLocation, 240, exportHeight);
        this.getContentPane().add(exportPathTextField);
        // ѡ��·����ť
        Button exportPathButton = new Button("...");
        exportPathButton.setBounds(340, exportYLocation, 40, exportHeight);
        exportPathButton.setFocusable(false);
        exportPathButton.addActionListener((event)->this.setPath(exportPathTextField, exportPathTextField.getText(), true));
        this.getContentPane().add(exportPathButton);


        // ��ť----------------------------------------
        int buttonYLocation = 168;
        int buttonHeight = 23;
        // ����
        Button exportButton = new Button("����");
        exportButton.setFont(new Font(font, Font.PLAIN, 12));
        exportButton.setBounds(75, buttonYLocation, 80, buttonHeight);
        exportButton.setFocusable(false);
        exportButton.addActionListener(event->{
            String sourcePath = sourcePathTextField.getText();
            File sourceFolder = new File(sourcePath);
            if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Դ�ļ�����������ԴĿ¼", "����", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String exportPath = exportPathTextField.getText();
            File exportFolder = new File(exportPath);
            if (!exportFolder.isDirectory()) {
                int result = JOptionPane.showConfirmDialog(this, "����·�������Ƿ񵼳���������ͬĿ¼��", "����", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    exportPath = this.defaultPath;
                } else {
                    return;
                }
            }
            new AlphaNumComparator().getList(sourcePathTextField.getText(), exportPath);
            if (openFolderCheckbox.getState()) {
                try {
                    java.awt.Desktop.getDesktop().open(exportFolder);
                } catch (Exception e){}
            }
            JOptionPane.showMessageDialog(this, "�����ɹ�", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
        });
        this.getContentPane().add(exportButton);

        // �ر�
        Button closeButton = new Button("�ر�");
        closeButton.setFont(new Font(font, Font.PLAIN, 12));
        closeButton.setBounds(236, buttonYLocation, 80, buttonHeight);
        closeButton.setFocusable(false);
        closeButton.addActionListener(event->System.exit(0));
        this.getContentPane().add(closeButton);
    }

    /**
     * �ļ�ѡ����
     * @param pathTextField // �ı������
     * @param defaultPath // Ĭ��·��
     * @param dictionaryOnly // �Ƿ����ʾ�ļ���
     */
    private void setPath(TextField pathTextField, String defaultPath, boolean dictionaryOnly) {
        JFileChooser jfc = new JFileChooser();
        File filePath = new File(defaultPath);
        jfc.setCurrentDirectory(filePath.exists() ? filePath : new File(this.defaultPath));
        jfc.setFileSelectionMode(dictionaryOnly ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showDialog(this, "ѡ��");
        File file = jfc.getSelectedFile();
        if (file.isDirectory()) {
            pathTextField.setText(file.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(this, "��ѡ���ļ��У�", "����", JOptionPane.ERROR_MESSAGE);
        }
    }
}
