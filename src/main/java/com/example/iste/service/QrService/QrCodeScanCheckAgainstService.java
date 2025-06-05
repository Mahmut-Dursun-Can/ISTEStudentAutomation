package com.example.iste.service.QrService;

import com.example.iste.entity.Appointment;
import com.example.iste.entity.QR.QrCode;
import com.example.iste.entity.QR.QrScan;
import com.example.iste.repository.QrRepository;
import com.example.iste.repository.QrScanRepository;
import com.example.iste.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class QrCodeScanCheckAgainstService {
    private final QrRepository qrRepository;
    private final QrScanRepository qrScanRepository;
    private final AppointmentService appointmentService;

    public QrCodeScanCheckAgainstService(QrRepository qrRepository,
                                         QrScanRepository qrScanRepository,
                                         AppointmentService appointmentService) {
        this.qrRepository = qrRepository;
        this.qrScanRepository = qrScanRepository;
        this.appointmentService = appointmentService;
    }



    @Transactional(readOnly = true)
    public boolean isLatestQrScanned() {
        Optional<QrCode> optionalQrCode = qrRepository
                .findFirstByExpiresAtAfterOrderByCreatedAtDesc(LocalDateTime.now());

        if (optionalQrCode.isEmpty()) {
            System.out.println("Geçerli QR kodu bulunamadı for user: " );
            return false;
        }

        QrCode qrCode = optionalQrCode.get();
        String token = qrCode.getToken();
        LocalDateTime expiresAt = qrCode.getExpiresAt();

        System.out.println("Found QR token: " + token + ", expires at: " + expiresAt);

        Optional<QrScan> optionalQrScan = qrScanRepository
                .findFirstByTokenOrderByScannedAtDesc(token);

        if (optionalQrScan.isEmpty()) {
            System.out.println("QR kodu hiç taranmadı: " + token);
            return false;
        }

        QrScan qrScan = optionalQrScan.get();
        LocalDateTime scannedAt = qrScan.getScannedAt();

        System.out.println("QR token scanned at: " + scannedAt);

        boolean valid = scannedAt.isBefore(expiresAt);
        System.out.println("QR kodu geçerli mi? " + valid);
        Appointment appointment = appointmentService.findByUsername(qrCode.getUsername());
        if (appointment == null) {
            System.out.println("Randevu alınmamış: " + qrCode.getUsername());
            return false;
        }

        return valid;
    }
}
