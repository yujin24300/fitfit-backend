package com.fitfit.server.api.workout.controller;

import com.fitfit.server.api.workout.dto.ExerciseRecordRequest;
import com.fitfit.server.api.workout.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercise-records")
@RequiredArgsConstructor
public class ExerciseRecordController {
    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<String> saveRecord(@RequestBody ExerciseRecordRequest request) {
        try {
            exerciseService.saveRecord(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Exercise record saved successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save exercise record");
        }
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteExerciseRecord(
            @PathVariable Long recordId,
            @RequestParam Long userId // 사용자의 본인 기록만 삭제하도록 userId 필요
    ) {
        exerciseService.deleteRecord(recordId, userId);
        return ResponseEntity.ok("Exercise record deleted successfully");
    }
}

