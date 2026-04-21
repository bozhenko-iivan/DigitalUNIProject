package ua.naukma.network.dto;

import java.io.Serializable;

public record TransferDTO(int studentId, int newGroupID) implements Serializable {
}
