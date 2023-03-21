package study.datajpa.repository;

/**
 * 중첩 구조
 * username과 teamname을 같이 가져옴
 */
public interface NestedClosedProjections {
    String getUsername();

    TeamInfo getTeam();

    interface TeamInfo{
        String getName();
    }
}
