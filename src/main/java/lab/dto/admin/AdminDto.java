package lab.dto.admin;

import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.user.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class AdminDto extends UserDto {
    List<BatchDto> batches;
    List<LabDto> labs;
    List<LabTestDto> labTests;
}