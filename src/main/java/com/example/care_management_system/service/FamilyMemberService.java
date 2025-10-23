package com.example.care_management_system.service;

import com.example.care_management_system.dto.FamilyMemberRequest;
import com.example.care_management_system.entity.FamilyMember;
import com.example.care_management_system.entity.User;
import com.example.care_management_system.exception.FamilyMemberNotFoundException;
import com.example.care_management_system.exception.FamilyMembersNotFoundException;
import com.example.care_management_system.repository.FamilyMemberRepository;
import com.example.care_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FamilyMemberService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private AuthService authService;

    public FamilyMember addFamilyMember(FamilyMemberRequest request) {
        String email = authService.getLoggedInUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        FamilyMember fm = new FamilyMember();
        fm.setName(request.getName());
        fm.setRelation(request.getRelation());
        fm.setGender(request.getGender());
        fm.setDateOfBirth(request.getDateOfBirth());
        fm.setUser(user);
        return familyMemberRepository.save(fm);
    }

    public FamilyMember updateFamilyMember(UUID id, FamilyMemberRequest request) {
        FamilyMember fm = familyMemberRepository.findById(id)
                .orElseThrow(() -> new FamilyMemberNotFoundException("Family member not found with id: " + id));

        fm.setName(request.getName());
        fm.setRelation(request.getRelation());
        fm.setGender(request.getGender());
        fm.setDateOfBirth(request.getDateOfBirth());
        return familyMemberRepository.save(fm);
    }

    public void deleteFamilyMember(UUID id) {
        FamilyMember fm = familyMemberRepository.findById(id)
                .orElseThrow(() -> new FamilyMemberNotFoundException("Family member not found with id: " + id));
        familyMemberRepository.delete(fm);
    }

    public List<FamilyMember> getFamilyMembersForLoggedInUser() {
        String email = authService.getLoggedInUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<FamilyMember> familyMembers = user.getFamilyMembers();

        if (familyMembers == null || familyMembers.isEmpty()) {
            throw new FamilyMembersNotFoundException("No family members found for user: " + email);
        }
        return familyMembers;
    }
}
